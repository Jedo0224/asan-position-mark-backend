package org.asanpositioningserver.global.common;//package org.asansocketserver.global.common;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import jakarta.validation.constraints.NotNull;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class ResponseMessage {
//
//    @NotNull
//    private int statusCode;
//
//    @NotNull
//    private String message;
//
//    @JsonIgnoreProperties({"hibernateLazyInitializer"})
//    private Object data;
//
//    public ResponseMessage(Integer statusCode, String message) {
//        this.statusCode = statusCode;
//        this.message = message;
//    }
//
//    public ResponseMessage(Integer statusCode, String message, Object data) {
//        this.statusCode = statusCode;
//        this.message = message;
//        this.data = data;
//    }
//}
