package org.asanpositioningserver.domain.position.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asanpositioningserver.domain.position.dto.ResultDataDTO;
import org.asanpositioningserver.domain.position.dto.request.*;
import org.asanpositioningserver.domain.position.dto.response.PositionResponseDto;
import org.asanpositioningserver.domain.position.entity.*;
import org.asanpositioningserver.domain.position.mongorepository.PositionMongoRepository;
import org.asanpositioningserver.domain.position.repository.BeaconDataRepository;
import org.asanpositioningserver.domain.position.repository.PositionStateRepository;
import org.asanpositioningserver.domain.position.util.BeaconDataUtil;
import org.asanpositioningserver.domain.position.util.UniqueBSSIDMap;
import org.asanpositioningserver.global.error.exception.EntityNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


import static org.asanpositioningserver.global.error.ErrorCode.WATCH_UUID_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PositionService {
    private final BeaconDataRepository beaconDataRepository;
    private final WatchService watchService;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final BeaconDataUtil beaconDataUtil;
    private final PositionStateRepository positionStateRepository;
    private final PositionMongoRepository positionMongoRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public static String UPLOAD_DIR = "C:\\Users\\AMC-guest\\uploads\\beacon_data\\";
    public List<BeaconCountsDTO> countBeacon() {
        return beaconDataRepository.findAllBeaconCount().stream()
                .map(result -> new BeaconCountsDTO((String) result[0], ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public void createCsv() throws JsonProcessingException {
        List<BeaconData> beaconDataList = beaconDataRepository.findAll();

        // 데이터를 저장할 Map
        Map<String, List<Map<String, String>>> data = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // 모든 비콘 데이터를 파싱하여 Map에 저장
        for (BeaconData reading : beaconDataList) {
            List<Map<String, String>> beaconDataListToMap = objectMapper.readValue(
                    reading.getBeaconData(), new TypeReference<List<Map<String, String>>>() {}
            );
            String position = reading.getPosition();
            data.putIfAbsent(position, new ArrayList<>());

            for (Map<String, String> beaconData : beaconDataListToMap) {
                data.get(position).add(beaconData);
            }
        }

        // 유니크한 BSSID를 수집
        Set<String> uniqueBssids = new TreeSet<>();  // TreeSet을 사용하여 자동으로 정렬
        for (List<Map<String, String>> beaconDataMapList : data.values()) {
            for (Map<String, String> beaconData : beaconDataMapList) {
                uniqueBssids.add(beaconData.get("bssid"));
            }
        }

        UniqueBSSIDMap.getInstance().initializeBSSIDMap(uniqueBssids);

        // CSV 파일 생성
        try (FileWriter writer = new FileWriter(UPLOAD_DIR + "output.csv")) {
            // 헤더 작성
            writer.append("Room");
            for (String bssid : uniqueBssids) {
                writer.append(",").append(bssid);
            }
            writer.append("\n");

            // 데이터 작성
            for (Map.Entry<String, List<Map<String, String>>> entry : data.entrySet()) {
                String position = entry.getKey();
                for (Map<String, String> beaconData : entry.getValue()) {
                    writer.append(position);
                    for (String bssid : uniqueBssids) {
                        writer.append(",");
                        String rssi = beaconData.get("bssid").equals(bssid) ? beaconData.get("rssi") : "NaN";
                        writer.append(rssi != null ? rssi : "NaN");
                    }
                    writer.append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    public void insertState(StateDTO stateDTO) {
        Long watchId = Long.valueOf(stateDTO.watchId());

        PositionState positionState =
                PositionState.createPositionState(Long.valueOf(stateDTO.watchId()), stateDTO.imageId(), stateDTO.position(), System.currentTimeMillis(),stateDTO.endTime());
        positionStateRepository.save(positionState);

        long currentTimeMillis = System.currentTimeMillis();
        long endTimeMillis = stateDTO.endTime();

        // Convert milliseconds to readable date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ZonedDateTime currentTime = Instant.ofEpochMilli(currentTimeMillis).atZone(ZoneId.systemDefault());
        ZonedDateTime endTime = Instant.ofEpochMilli(endTimeMillis).atZone(ZoneId.systemDefault());

        System.out.println("stateDTO.endTime() : " + endTime.format(formatter));
        System.out.println("System.currentTimeMillis() = " + currentTime.format(formatter));

        long delay = stateDTO.endTime() - System.currentTimeMillis();
        if (delay > 0) {
            ScheduledFuture<?> scheduledTask = scheduler.schedule(() -> {
                positionStateRepository.deleteById(watchId);
                scheduledTasks.remove(watchId);
            }, delay, TimeUnit.MILLISECONDS);

            // 기존 예약된 작업이 있다면 취소
            ScheduledFuture<?> existingTask = scheduledTasks.put(watchId, scheduledTask);
            if (existingTask != null) {
                existingTask.cancel(false);
            }
        }
    }

    public void deleteState(StateDTO stateDTO) {
        Long watchId = Long.valueOf(stateDTO.watchId());
        positionStateRepository.deleteById(watchId);
    }

    public PositionState getCollectionState(Long watchId) {

        PositionState positionState = positionStateRepository.findById(watchId).orElse(null);

        // 이 조건문이 과연 필요한가?
        if (positionState == null)
            return PositionState.createPositionState(watchId,null,null,null,0L);
        else
            return positionState;
    }


    public PositionResponseDto receiveData(PosDataDTO posData) throws Exception {
        String responseDto;
        System.out.println("posData.beaconData() = " + posData.beaconData());
        Long watchId = Long.valueOf(posData.watchId());
        String watchName;

        WatchLive watchLive = watchService.findById(watchId);
        if (watchLive == null) {
            watchName = "지정되지않음";
        } else {
            watchName = watchLive.getWatchName();
        }

        PositionState positionState = findByPositionStateOrNull(watchId);
        UniqueBSSIDMap baseMap = UniqueBSSIDMap.getInstance();
        UniqueBSSIDMap uniqueBSSIDMap = new UniqueBSSIDMap();
        String prediction;
        synchronized (baseMap) {
            uniqueBSSIDMap.copyFrom(baseMap);

            try {
                // Initialize uniqueBSSIDMap with unique BSSIDs if needed
                if (!Objects.isNull(positionState)) {
                    responseDto = addPosData(posData, positionState.getImageId(), positionState.getPosition());
                } else {
                    for (BeaconDataDTO beaconData : posData.beaconData()) {
                        System.out.println("Updating beaconData bssid = " + beaconData.bssid() + ", rssi = " + beaconData.rssi());
                        uniqueBSSIDMap.updateBSSIDMap(beaconData.bssid(), String.valueOf(beaconData.rssi()));
                    }

                    responseDto = findPosition(posData);
                    PositionData positionData = PositionData.of(responseDto);
                    updatePositionData(watchId, positionData);
                }
            } finally {
                System.out.println("Before copying to baseMap: " + uniqueBSSIDMap.getBSSIDMap());
                baseMap.copyFrom(uniqueBSSIDMap);
                System.out.println("After copying to baseMap: " + baseMap.getBSSIDMap());

                prediction = "null";
                if (!baseMap.getBSSIDMap().isEmpty()) {
                    prediction = sendUniqueBSSIDMapToFlask(uniqueBSSIDMap);
                }


                baseMap.resetBSSIDMapValues();

                System.out.println("After reset: " + baseMap.getBSSIDMap());
            }
        }

        if(posData.beaconData().isEmpty()){
            prediction = "null";
        }

        System.out.println("watchName + \" \" + responseDto = " + watchName + " " + prediction);
        return PositionResponseDto.of(watchId, watchName, prediction);
    }

    private String sendUniqueBSSIDMapToFlask(UniqueBSSIDMap uniqueBSSIDMap) throws JSONException {
        String flaskUrl = "http://localhost:5000/predict";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> bssidMap = uniqueBSSIDMap.getBSSIDMap();
        HttpEntity<Map<String, String>> request = new HttpEntity<>(bssidMap, headers);

        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, request, String.class);
        JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getString("prediction");
    }


    private PositionState findByPositionStateOrNull(Long id) {
        return positionStateRepository.findById(id)
                .orElse(null);
    }

    public void deleteBeacon(String positionName) {
        List<BeaconData> beaconsByPosition = beaconDataRepository.findAllByPosition(positionName);
        beaconDataRepository.deleteAll(beaconsByPosition);
    }

    private String addPosData(PosDataDTO posData, Long imageId,String position) {
        if (posData.beaconData().isEmpty()){
            return null;
        }
        BeaconData beaconDataEntity = new BeaconData();
        beaconDataEntity.setImageId(imageId);
        beaconDataEntity.setPosition(position);
        String beaconDataJson = converBeaconDataDtoToJson(posData.beaconData());
        beaconDataEntity.setBeaconData(beaconDataJson);
        beaconDataRepository.save(beaconDataEntity);
        return null;
    }

    // 받은 PosData에서 json({uuid, rssi})을 (DB)에 저장.
    private String converBeaconDataDtoToJson(List beaconDataDTO) {
        // ObjectMapper를 사용하여 Beacon
        // DataDTO를 JSON 문자열로 변환
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(beaconDataDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String findPosition(PosDataDTO data) {
        List<BeaconData> dbDataList = beaconDataRepository.findAll();

        List<Future<List<ResultDataDTO>>> futureResults = new ArrayList<>();

        if (data.beaconData().isEmpty()){
            return null;
        }

        //클라이언트가 제공한 와이파이 데이터와 데이터베이스에 저장된 와이파이 데이터를 빠르게 비교하기 위해 다중 스레딩 사용.
        int threadNum = Runtime.getRuntime().availableProcessors();
        int sliceLen = (int) Math.ceil((double) dbDataList.size()) / threadNum;
        int knn = 7;


        for (int i = 0; i < threadNum-1; i++) {
            int start = sliceLen * i;
            int end = Math.min(start + sliceLen, dbDataList.size());
            List<BeaconData> slicedDataList = dbDataList.subList(start, end);
            Future<List<ResultDataDTO>> future = taskExecutor.submit(() -> calPos(slicedDataList, data, 0.4));
            futureResults.add(future);
        }

        List<ResultDataDTO> results = futureResults.stream()
                .flatMap(future -> {
                    try {
                        return future.get().stream();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new IllegalStateException("Thread interrupted", e);
                    }
                }).collect(Collectors.toList());

        return calcKnn(results, knn);
    }


    // 클라이언트의 와이파이 데이터와 데이터베이스의 와이파이 데이터를 비교하여, 가장 가능성이 높은 위치 정보를 담은 리스트를 반환
    private List<ResultDataDTO> calPos(List<BeaconData> dbDataList, PosDataDTO inputData, double margin) {
        List<ResultDataDTO> resultList = new ArrayList<>();
        int largestCount = 0;
        for (BeaconData dbData : dbDataList) {
            List<BeaconDataDTO> dbBeaconDataList = beaconDataUtil.parseBeaconData(dbData.getBeaconData());
            int count = 0;
            int sum = 0;

            for (BeaconDataDTO dbBeaconData : dbBeaconDataList) {
                for (BeaconDataDTO inputBeaconData : inputData.beaconData()) {
                    if (dbBeaconData.bssid().equals(inputBeaconData.bssid())) {

                        count++;
                        sum += Math.abs(dbBeaconData.rssi() - inputBeaconData.rssi());
                        break;
                    }
                }
            }

            double avg = count > 0 ? (double) sum / count : Double.MAX_VALUE;
            double ratio = count > 0 ? avg / count : Double.MAX_VALUE;


            resultList.add(new ResultDataDTO(dbData.getId(), dbData.getPosition(), count, avg, ratio));
            largestCount = Math.max(largestCount, count);
        }


        int finalLargestCount = largestCount;

        return resultList.stream()
                .filter(data -> data.getCount() >= finalLargestCount * margin)  // * margin)
                .sorted(Comparator.comparingDouble(ResultDataDTO::getRatio))
                .collect(Collectors.toList());
    }

    // calpos 결과값을 기반으로 k개의 이웃값과 비교하여 최적값 반환.
    private String calcKnn(List<ResultDataDTO> results, int k) {
        // 결과를 ratio 오름차순으로 정렬
        List<ResultDataDTO> sortedResults = results.stream()
                .sorted(Comparator.comparingInt(ResultDataDTO::getCount).reversed()
                        .thenComparingDouble(ResultDataDTO::getRatio))
                .toList();




        // 가장 가까운 k개의 이웃을 선택
        List<ResultDataDTO> nearestNeighbors = sortedResults.stream()
                .limit(k)
                .toList();

//        nearestNeighbors.forEach(result ->
//                System.out.println("ID: " + result.getId() + ", Position: " + result.getPosition() +
//                        ", Count: " + result.getCount() + ", Avg: " + result.getAvg() +
//                        ", Ratio: " + result.getRatio()));

        // 이웃들 중에서 위치별 투표 수 계산
        Map<String, Integer> positionVotes = new HashMap<>();
        for (ResultDataDTO neighbor : nearestNeighbors) {
            String position = neighbor.getPosition();
            positionVotes.put(position, positionVotes.getOrDefault(position, 0) + 1);
//            System.out.println("positionVotes = " + positionVotes);
        }

        // 가장 많이 투표된 위치를 찾음
        String bestPosition = null;
        int maxVotes = -1;

        for (Map.Entry<String, Integer> entry : positionVotes.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                bestPosition = entry.getKey();
            }
        }

//        System.out.println("bestPosition = " + bestPosition);

        if (bestPosition != null) {
            for (ResultDataDTO result : nearestNeighbors) {
                if (result.getPosition().equals(bestPosition)) {
                    log.info("[resultHashMap]::" + result.getPosition());
                    return result.getPosition();
                }
            }
        }
        return null;
    }

//    private Watch findByWatchOrThrow(String id) {
//        return watchRepository.findById(Long.parseLong(id))
//                .orElseThrow(() -> new EntityNotFoundException(WATCH_UUID_NOT_FOUND));
//    }

    private void updatePositionData(Long watchId, PositionData position) {
        positionMongoRepository.updatePosition(watchId, position);
    }
}




