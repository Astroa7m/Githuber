package com.astroscoding.sharing.presentation

import android.content.Intent
import android.content.pm.verify.domain.DomainVerificationManager
import android.content.pm.verify.domain.DomainVerificationUserState
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    CheckIfAppApprovedForDomain()
                SharingRepoScreen()

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    private fun CheckIfAppApprovedForDomain(
        domain: String = getString(R.string.host),
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        var launchDialog by remember { mutableStateOf(false) }
        val manager = remember { context.getSystemService(DomainVerificationManager::class.java) }
        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    val userState = manager.getDomainVerificationUserState(context.packageName)
                    val verifiedDomain =
                        userState?.hostToStateMap?.filterValues { it == DomainVerificationUserState.DOMAIN_STATE_SELECTED }
                    val selectedDomains = userState?.hostToStateMap
                        ?.filterValues { it == DomainVerificationUserState.DOMAIN_STATE_SELECTED }
                    launchDialog =
                        (verifiedDomain?.keys?.contains(domain) != true || selectedDomains?.keys?.contains(domain) != true)
                         || userState.isLinkHandlingAllowed == false
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
        if (launchDialog) {
            VerifyDomainDialog(
                onDismissed = {
                    launchDialog = false
                },
                onConfirm = {
                    val intent = Intent(
                        Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                        Uri.parse("package:${context.packageName}")
                    )
                    context.startActivity(intent)
                }
            )
        }
    }

    @Composable
    private fun VerifyDomainDialog(
        modifier: Modifier = Modifier,
        onDismissed: () -> Unit,
        onConfirm: () -> Unit
    ) {

        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            modifier = modifier,
            title = {
                Text(
                    text = "Extra Configurations Needed!"
                )
            },
            text = {
                   Text(text = "To provide the best user experience with this feature, please do the following:\n" +
                           "1 - Click on \"Open Settings\" below.\n" +
                           "2 - Make sure \"Open supported links\" is switched ON.\n" +
                           "3 - Make sure you checked all the links in the dialog after clicking on \"+ Add link\".\n\n" +
                           "Note: You could simply ignore setting the app as the default links handler. Though you won't be able to navigate to the app by clicking on external app-related links")
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissed) {
                    Text(text = "Ignore")
                }
            }
        )
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
                                        shareRepositoryUrl(repoToShare)
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

    private suspend fun shareRepositoryUrl(repo: RepoToShare) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getRepoUrl(repo.name))
            putExtra(Intent.EXTRA_TITLE, repo.description)
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