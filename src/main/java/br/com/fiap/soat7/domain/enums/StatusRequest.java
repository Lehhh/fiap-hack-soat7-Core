package br.com.fiap.soat7.domain.enums;

import lombok.Getter;

@Getter
public enum StatusRequest {
	DISK_STATUS("disk/status"),
	UPLOAD_S3_STATUS("video/s3/status"),
	UPLOAD_S3_FIX("video/s3/fix"),
	PROCESS_VIDEO_STATUS("video/process/status"),
	PROCESS_VIDEO_FIX("video/process/fix"),
	UPLOAD_S3_IMAGES_STATUS("images/status"),
	UPLOAD_S3_IMAGES_FIX("images/fix");

	private final String endPoint;
	StatusRequest(String endPoint) {
		this.endPoint = endPoint;
	}
}
