package org.binchoo.paimonganyu.hoyoapi.ds;

import lombok.Builder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Builder
public final class DsHeaderGenerator {

    private final static String HEADER_DS = "DS";
    private final static String HEADER_X_RPC_LANGUAGE = "x-rpc-language";
    private final static String HEADER_X_RPC_APP_VERSION = "x-rpc-app_version";
    private final static String HEADER_X_RPC_CLIENT_TYPE = "x-rpc-client_type";

    private final DsGenerator dsGenerator;

    @Builder.Default
    private String xRpcLang = "ko-kr";

    @Builder.Default
    private String xRpcAppVersion = "1.5.0";

    @Builder.Default
    private int xRpcClientType= 5;

    public MultiValueMap<String, String> generateDsHeader() {
        MultiValueMap<String, String> headers = this.getXrpcHeader();
        headers.add(HEADER_DS, this.dsGenerator.generateDs());
        return headers;
    }

    public MultiValueMap<String, String> generateDsHeader(String salt) {
        MultiValueMap<String, String> headers = this.getXrpcHeader();
        headers.add(HEADER_DS, this.dsGenerator.generateDs(salt));
        return headers;
    }

    private MultiValueMap<String, String> getXrpcHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HEADER_DS, this.dsGenerator.generateDs());
        headers.add(HEADER_X_RPC_LANGUAGE, this.xRpcLang);
        headers.add(HEADER_X_RPC_APP_VERSION, this.xRpcAppVersion);
        headers.add(HEADER_X_RPC_CLIENT_TYPE, String.valueOf(this.xRpcClientType));
        return headers;
    }

    public String getxRpcLang() {
        return xRpcLang;
    }

    public String getxRpcAppVersion() {
        return xRpcAppVersion;
    }

    public int getxRpcClientType() {
        return xRpcClientType;
    }
}
