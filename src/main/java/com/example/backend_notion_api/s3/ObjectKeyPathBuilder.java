package com.example.backend_notion_api.s3;

public class ObjectKeyPathBuilder {

    private static final String BASE_PATH_FORMAT = "%s/%s";
    // 예 : {page_id}/{file_id}

    public static String buildObjectKey(String pageId, String fileId){
        return String.format(BASE_PATH_FORMAT, pageId, fileId);
    }

}
