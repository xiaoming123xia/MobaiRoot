package com.mobai.root.data.repository

import com.mobai.root.data.model.RepoModule

interface ModuleRepoRepository {
    suspend fun fetchModules(): Result<List<RepoModule>>
}
