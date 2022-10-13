package com.github.avpyanov.tools.testit.client.api;

import com.github.avpyanov.tools.testit.client.dto.Attachment;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.io.File;

public interface Attachments {

    @Headers("Content-Type: multipart/form-data")
    @RequestLine("POST /api/v2/attachments")
    Attachment createAttachment(@Param(value = "file") File attachment);
}