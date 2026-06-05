package com.mobai.root.data.repository

import com.mobai.root.data.model.Module
import com.mobai.root.data.model.ModuleUpdateInfo

interface ModuleRepository {
    suspend fun getModules(): Result<List<Module>>
    suspend fun checkUpdate(module: Module): Result<ModuleUpdateInfo>
}
