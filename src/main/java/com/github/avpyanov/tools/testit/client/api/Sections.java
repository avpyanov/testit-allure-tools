package com.github.avpyanov.tools.testit.client.api;

import com.github.avpyanov.tools.testit.client.dto.Section;
import com.github.avpyanov.tools.testit.client.dto.SectionPostDto;
import feign.Headers;
import feign.RequestLine;

public interface Sections {

    @Headers("Content-Type: application/json")
    @RequestLine("POST /api/v2/sections")
    Section createSection(SectionPostDto section);
}