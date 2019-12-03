package top.jach.tes.core.repository;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.dto.PageQueryDto;

import java.util.List;

public interface InfoRepository<I extends Info, C> extends Repository {

    /**
     * 存储Info Info已经赋予了Id
     * @param info
     * @return
     */
    I saveProfile(I info, Long projectId);

    /**
     * 存储Info Info的Profile部分已经被存储
     * @param info
     * @return
     */
    I saveDetail(I info);

    /**
     * 根据Id更新Info的概要信息，不包括详细数据
     * @param info
     * @return
     */
    I updateProfileByInfoId(I info);

    /**
     * 根据Id删除Info
     * @param infoId
     * @return
     */
    I deleteByInfoId(Long infoId);

    /**
     * 根据Info中的非空属性和projectId(如果非空的话)，通过与的方式进行查询
     * 当pageNum为0时，相当于查询数量
     * @param info
     * @param projectId
     * @return
     */
    PageQueryDto<I> queryProfileByInfoAndProjectId(Info info, Long projectId, PageQueryDto pageQueryDto);

    /**
     * 定制化的查询
     * @param c
     * @param pageQueryDto
     * @return
     */
    PageQueryDto<I> queryProfileByCustom(C c, PageQueryDto pageQueryDto);

    /**
     * 查询Info具体数据，需要将Info中的Detail进行填充
     * @param infoIds
     * @return
     */
    List<I> queryDetailsByInfoIds(List<Long> infoIds);
}
