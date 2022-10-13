package com.github.avpyanov.tools.testit.client.api;

import com.github.avpyanov.tools.testit.client.dto.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface Autotests {

    @RequestLine("GET /api/v2/autoTests/{autoTestId}")
    AutotestDto getAutoTest(@Param("autoTestId") String autoTestId);

    @Headers("Content-Type: application/json")
    @RequestLine("POST /api/v2/autoTests")
    AutotestDto createAutotest(AutotestPostRequestDto autotest);

    @Headers("Content-Type: application/json")
    @RequestLine("PUT /api/v2/autoTests")
    AutotestDto updateAutotest(AutotestPutRequestDto autotest);

    @RequestLine("DELETE /api/v2/autoTests/{autotestId}")
    void deleteAutotest(@Param("autotestId") String autotestId);

    @RequestLine("GET /api/v2/autoTests/{autotestId}/workItems?isWorkItemDeleted={isWorkItemDeleted}")
    List<ItemId> getLinkedWorkItems(@Param("autotestId") String autotestId,
                                    @Param("isWorkItemDeleted") boolean isWorkItemDeleted);

    @Headers("Content-Type: application/json")
    @RequestLine("POST /api/v2/autoTests/{autotestId}/workItems")
    void linkAutotestAndWorkItem(@Param("autotestId") String autotestId, IdDto id);
}