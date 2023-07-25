package ur.azizairo.foundation.model.tasks

import ur.azizairo.foundation.model.Repository

typealias TaskBody<T> = () -> T

interface TasksFactory: Repository {

    fun <T> async(body: TaskBody<T>): Task<T>

}
