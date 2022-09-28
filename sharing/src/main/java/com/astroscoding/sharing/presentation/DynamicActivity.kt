package com.astroscoding.sharing.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.astroscoding.common.R
import com.astroscoding.common.domain.model.Repo
import com.astroscoding.common.presentation.ui.theme.GithuberTheme
import com.astroscoding.di.SharingModuleDependencies
import com.astroscoding.sharing.di.DaggerSharingComponent
import com.astroscoding.sharing.di.ViewModelFactory
import com.astroscoding.sharing.model.RepoToShare
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
class DynamicActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<DynamicActivityViewModel> { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSharingComponent.builder()
            .context(this)
            .moduleDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    SharingModuleDependencies::class.java
                )
            ).build()
            .inject(this)

        setContent {
            GithuberTheme {

                LaunchedEffect(key1 = Unit) {
                    viewModel.repo.value = (intent.extras?.getParcelable("repo"))
                }

                SharingRepoScreen()

            }
        }
    }

    @Composable
    private fun SharingRepoScreen(
        modifier: Modifier = Modifier,
        repo: Repo? = viewModel.repo.value
    ) {
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Sharing Repository",
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                        )
                    }
                )
            }
        ) { paddingValues ->
            Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
                repo?.let {
                    RepoToShare.mapFromDomain(it).also { repoToShare ->
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp)
                                .scrollable(rememberScrollState(), Orientation.Vertical),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                AsyncImage(
                                    model = repoToShare.ownerAvatarUrl,
                                    contentDescription = "${repoToShare.name}'s profile url",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = repoToShare.description,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Button(
                                onClick = {
                                        scope.launch {
                                            shareRepositoryUrl(repoToShare.name)
                                        }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Share")
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun shareRepositoryUrl(repoName: String) {
        Intent(Intent.ACTION_SEND).apply{
            putExtra(Intent.EXTRA_TEXT, getRepoUrl(repoName))
            type = "text/plain"
            Intent.createChooser(this, null)
            startActivity(this)
        }
    }

    private suspend fun getRepoUrl(repoName: String): String {
        val language = viewModel.currentLanguage.first()
        val host = getString(R.string.host)
        val scheme = getString(R.string.scheme)
        val path = getString(R.string.path)
        return "$scheme://$host$path?repoName=$repoName&language=$language"
    }
}