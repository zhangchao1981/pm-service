package com.iscas.pm.api.service;

import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhangchao
* @description 针对表【task_feedback(任务反馈表)】的数据库操作Service
* @createDate 2022-08-02 09:37:30
*/
public interface TaskFeedbackService extends IService<TaskFeedback> {
    List<TaskFeedback> selectListByTaskId(TaskFeedback taskFeedback);

    void saveTaskFeedback(TaskFeedback taskFeedback);


}
