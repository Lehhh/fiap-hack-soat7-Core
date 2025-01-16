package br.com.fiap.soat7.infrastructure.repository;

import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.domain.VideoProcess;
import br.com.fiap.soat7.domain.enums.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VideoProcessRepository extends JpaRepository<VideoProcess, Long> {
	@Modifying
	@Query("UPDATE VideoProcess v SET v.stageUploadS3 = :stage, v.updatedDate = CURRENT_TIMESTAMP WHERE v.id = :videoId")
	int updateS3VideoStatus(@Param("videoId") Long videoId, @Param("stage") Stage stage);

	@Modifying
	@Query("UPDATE VideoProcess v SET v.stageProcessVideo = :stage, v.updatedDate = CURRENT_TIMESTAMP WHERE v.id = :videoId")
	int updateProcessVideoStatus(@Param("videoId") Long videoId, @Param("stage") Stage stage);

	@Modifying
	@Query("UPDATE ImageProcess i SET i.stage = :stage WHERE i.id = :imageId")
	int updateImageStatus(@Param("imageId") Long imageId, @Param("stage") Stage stage);

	@Query("SELECT v.version FROM VideoProcess v WHERE v.name = :name AND v.userId.id = :userId ORDER BY v.version DESC LIMIT 1")
	Optional<Integer> findLastVersion(@Param("name") String name, @Param("userId") Long userId);
	@Modifying
	@Transactional
	@Query("UPDATE VideoProcess v SET v.updatedDate = CURRENT_TIMESTAMP, v.version = :version WHERE v.name = :name AND v.userId.id = :userId")
	void updateVersion( @Param("userId")Long userId, @Param("name")String name, @Param("version")Integer version);

	@Query("SELECT v FROM VideoProcess v WHERE v.name = :name AND v.userId.id = :userId")
	VideoProcess findByNameAndUserId(@Param("userId") Long userId, @Param("name") String name);
}
