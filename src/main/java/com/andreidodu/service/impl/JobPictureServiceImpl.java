package com.andreidodu.service.impl;

import com.andreidodu.dto.JobPictureDTO;
import com.andreidodu.exception.ApplicationException;
import com.andreidodu.mapper.JobPictureMapper;
import com.andreidodu.model.Job;
import com.andreidodu.model.JobPicture;
import com.andreidodu.repository.JobPictureRepository;
import com.andreidodu.repository.JobRepository;
import com.andreidodu.service.JobPictureService;
import com.andreidodu.util.CommonValidationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.REQUIRED)
public class JobPictureServiceImpl implements JobPictureService {

    private final JobPictureRepository jobPictureRepository;
    private final JobRepository jobRepository;
    private final JobPictureMapper jobPictureMapper;

    private static final Supplier<ApplicationException> supplyJobPictureNotFoundException = () -> new ApplicationException("JobPicture not found");
    private static final Supplier<ApplicationException> supplyJobNotFoundException = () -> new ApplicationException("Job not found");

    private Function<Long, Optional<JobPicture>> retrieveJobPicture;
    private Function<JobPicture, JobPictureDTO> saveJobPicture;
    private Function<Long, Optional<Job>> retrieveJob;

    @PostConstruct
    private void postConstruct() {
        saveJobPicture = (jobPicture) -> this.jobPictureMapper.toDTO(this.jobPictureRepository.save(jobPicture));
        retrieveJobPicture = jobPictureRepository::findById;
        retrieveJob = this.jobRepository::findById;
    }

    @Override
    public JobPictureDTO get(Long jobPictureId) throws ApplicationException {
        JobPicture jobPicture = checkExistence(jobPictureId);
        return this.jobPictureMapper.toDTO(jobPicture);
    }

    @Override
    public void delete(Long jobPictureId) throws ApplicationException {
        JobPicture jobPicture = checkExistence(jobPictureId);
        this.jobPictureRepository.delete(jobPicture);
    }

    private JobPicture checkExistence(Long jobPictureId) {
        return retrieveJobPicture.apply(jobPictureId)
                .orElseThrow(supplyJobPictureNotFoundException);
    }

    @Override
    public JobPictureDTO save(JobPictureDTO jobPictureDTO) throws ApplicationException {
        Job job = retrieveJob.apply(jobPictureDTO.getJobId())
                .orElseThrow(supplyJobNotFoundException);

        JobPicture model = this.jobPictureMapper.toModel(jobPictureDTO);
        model.setJob(job);

        return saveJobPicture.apply(model);
    }

    @Override
    public JobPictureDTO update(Long id, JobPictureDTO jobPictureDTO) throws ApplicationException {
        validateJobPictureIdMatching(id, jobPictureDTO);

        JobPicture jobPicture = checkExistence(id);

        copyJobPictureDtoPropertiesToModel(jobPictureDTO, jobPicture);

        return saveJobPicture.apply(jobPicture);

    }

    private void copyJobPictureDtoPropertiesToModel(JobPictureDTO jobPictureDTO, JobPicture jobPicture) {
        this.jobPictureMapper.getModelMapper().map(jobPictureDTO, jobPicture);
    }

    private static void validateJobPictureIdMatching(Long id, JobPictureDTO jobPictureDTO) throws ApplicationException {
        if (!CommonValidationUtil.isSameId.test(id, jobPictureDTO.getId())) {
            throw new ApplicationException("id not matching");
        }
    }


}
