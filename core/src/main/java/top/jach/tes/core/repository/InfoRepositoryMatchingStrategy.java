package top.jach.tes.core.repository;

import top.jach.tes.core.domain.info.Info;

public interface InfoRepositoryMatchingStrategy {
    AllInfoRepository matching(Info info);
}
