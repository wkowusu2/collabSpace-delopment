package com.collabspace.collabspace.utils;

import com.collabspace.collabspace.dto.AttachmentDto;
import com.collabspace.collabspace.entity.Attachment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AttachmentMapper {

    public static AttachmentDto toDto(Attachment a) {
        if (a == null)
            return null;
        AttachmentDto d = new AttachmentDto();
        d.setId(a.getId());
        d.setFileName(a.getFileName());
        d.setFileUrl(a.getFileUrl());
        d.setFileType(a.getFileType());
        d.setFileSize(a.getFileSize());
        d.setUploadedBy(a.getUploadedBy());
        d.setUploadedAt(a.getUploadedAt());
        return d;
    }

    public static List<AttachmentDto> toDtoList(List<Attachment> list) {
        if (list == null || list.isEmpty())
            return Collections.emptyList();
        return list.stream().map(AttachmentMapper::toDto).collect(Collectors.toList());
    }
}
