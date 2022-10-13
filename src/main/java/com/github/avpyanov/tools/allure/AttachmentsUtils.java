package com.github.avpyanov.tools.allure;

import com.github.avpyanov.tools.Settings;
import com.github.avpyanov.tools.testit.client.TestItApiClient;
import com.github.avpyanov.tools.testit.client.dto.Attachment;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AttachmentsUtils {

    private static final Logger logger = LogManager.getLogger(AttachmentsUtils.class);

    private static final Settings settings = ConfigFactory.create(Settings.class);
    private static final TestItApiClient testItClient = new TestItApiClient(settings.endpoint(), settings.token());


    public static List<Attachment> uploadAttachments(final List<io.qameta.allure.model.Attachment> allureAttachmentList) {
        List<Attachment> attachments = new ArrayList<>();
        for (io.qameta.allure.model.Attachment attachment : allureAttachmentList) {
            String filePath = String.format(settings.allureResultsPattern(), attachment.getSource());
            try {
                Attachment uploadedAttachment = testItClient.attachmentsApi().createAttachment(new File(filePath));
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