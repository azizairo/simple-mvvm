package ur.azizairo.foundation

import ur.azizairo.foundation.model.Repository

interface BaseApplication {

    val repositories: List<Repository>
}