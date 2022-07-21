package org.binchoo.paimonganyu.chatbot.resources;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jbinchoo
 * @since 2022-06-13
 */
public final class Images {

    private final Map<String, String> images;

    /**
     * @param images 이미지 이름 대 S3 주소 매핑 테이블
     */
    public Images(Map<String, String> images) {
        this.images = new HashMap<>(images);
    }

    /**
     * @param imageName 이미지 이름
     * @return 이미지 S3 주소
     */
    public String findByName(String imageName) {
        return images.get(imageName);
    }

    /**
     * @return 모든 이미지에 대한 이미지 이름 대 S3 주소 매핑 테이블
     */
    public Map<String, String> findAll() {
        return Collections.unmodifiableMap(images);
    }
}
