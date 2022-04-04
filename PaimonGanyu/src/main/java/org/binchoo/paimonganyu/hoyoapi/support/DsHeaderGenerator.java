package org.binchoo.paimonganyu.hoyoapi.support;

import lombok.Builder;
import org.binchoo.paimonganyu.hoyoapi.ds.DsGenerator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Builder
public final class DsHeaderGenerator {

    private final static String HEADER_DS = "DS";
    private final static String HEADER_X_RPC_LANGUAGE = "x-rpc-language";
    private final static String HEADER_X_RPC_APP_VERSION = "x-rpc-app_version";
    private final static String HEADER_X_RPC_CLIENT_TYPE = "x-rpc-client_type";

    @Builder.Default
    private final DsGenerator dsGenerator = new DefaultDsGenerator();

    @Builder.Default
    private String xRpcLang = "ko-kr";

    @Builder.Default
    private String xRpcAppVersion = "1.5.0";

    @Builder.Default
    private int xRpcClientType= 5;

    /**
     * DS 헤더를 만듭니다.
     * @return DS 값과 XRPC 파라미터가 담긴 DS 헤더
     */
    public MultiValueMap<String, String> generateDsHeader() {
        return generateDsHeader(null);
    }

    /**
     * DS 헤더를 만듭니다.
     * @param salt DS 솔트. null일 경우 DsGeneerator의 기본 DS 솔트를 사용합니다.
     * @return DS 값과 XRPC 파라미터가 담긴 DS 헤더
     */
    public MultiValueMap<String, String> generateDsHeader(String salt) {
        MultiValueMap<String, String> headers = this.getXrpcHeader();
        if (salt == null)
            headers.add(HEADER_DS, this.dsGenerator.generateDs());
        else
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

    public static DsHeaderGenerator getDefault() {
        return builder().build();
    }
}
