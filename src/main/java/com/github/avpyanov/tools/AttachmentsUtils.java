package com.github.avpyanov.tools;

import com.github.avpyanov.testit.client.dto.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AttachmentsUtils {

    private static final Logger logger = LogManager.getLogger(AttachmentsUtils.class);


    public static List<Attachment> uploadAttachments(final List<io.qameta.allure.model.Attachment> allureAttachmentList) {
        List<Attachment> attachments = new ArrayList<>();
        for (io.qameta.allure.model.Attachment attachment : allureAttachmentList) {
            String filePath = String.format(AllureConfig.getAllureResultsPattern(), attachment.getSource());
            try {
                com.github.avpyanov.testit.client.dto.Attachment uploadedAttachment = AllureConfig.getTestItApiClient()
                        .attachmentsApi().createAttachment(new File(filePath));
                attachments.add(uploadedAttachment);
            } catch (Exception e) {
                logger.error("Не удалось загрузить вложение {}, {}", filePath, e);
            }
        }
        return attachments;
    }

    private AttachmentsUtils() {
    }
}