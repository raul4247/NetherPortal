package com.raulfm.netherportal.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.raulfm.netherportal.R
import com.raulfm.netherportal.backend.api.controllers.ServerController
import com.raulfm.netherportal.backend.singletons.CooldownManager
import com.raulfm.netherportal.extensions.setState
import com.raulfm.netherportal.model.InstanceInfo
import com.raulfm.netherportal.model.MinecraftInfo
import com.raulfm.netherportal.model.ServerAction
import com.raulfm.netherportal.model.enums.InstanceStatus
import com.raulfm.netherportal.ui.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_server_control.*
import kotlinx.android.synthetic.main.fragment_server_control.view.*
import kotlinx.android.synthetic.main.info_card_instance_info.view.*
import kotlinx.android.synthetic.main.info_card_minecraft_server.view.*


class ServerControlFragment : Fragment() {
    private lateinit var root: View

    private lateinit var notAvailableLabelValue: String
    private lateinit var noneLabelValue: String
    private lateinit var yesLabelValue: String
    private lateinit var noLabelValue: String
    private lateinit var errorOccurred: String
    private lateinit var expiredSession: String

    companion object {
        fun newInstance(): ServerControlFragment = ServerControlFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_server_control, container, false)

        initFragmentResources()
        initFragmentComponents()
        getSeverStatus()
        root.cooldownProgressBar.progress = 100

        return root
    }

    private fun initFragmentResources() {
        notAvailableLabelValue =
            root.context.resources.getString(R.string.not_available_label_value)
        noneLabelValue = root.context.resources.getString(R.string.none_label_value)
        yesLabelValue = root.context.resources.getString(R.string.yes_label_value)
        noLabelValue = root.context.resources.getString(R.string.no_label_value)
        errorOccurred = root.context.resources.getString(R.string.error_ocurred)
        expiredSession = root.context.resources.getString(R.string.expired_session)
    }

    private fun initFragmentComponents() {
        root.openButton.setOnClickListener { sendServerAction(ServerAction.OPEN) }
        root.closeButton.setOnClickListener { sendServerAction(ServerAction.CLOSE) }
        root.rebootButton.setOnClickListener { sendServerAction(ServerAction.REBOOT) }
        root.swipeRefreshServerControls.setOnRefreshListener { getSeverStatus() }
        root.openButton.visibility = View.GONE
        root.closeButton.visibility = View.GONE
        root.rebootButton.visibility = View.GONE
    }

    private fun getSeverStatus() {
        setLoadingStatus(true)
        ServerController.getServerStatus(
            onSuccess = { serverData ->
                if (serverData == null) redirectToLogin(errorOccurred)
                serverData?.let {
                    setLoadingStatus(false)
                    swipeRefreshServerControls.isRefreshing = false
                    setInstanceInfoValues(it.instance_info)
                    setMinecraftServerValues(it.minecraft_info)
                    showButtonsForStatus(it.instance_info.status)
                }
            },
            onBadCredentials = {
                redirectToLogin(expiredSession)
            },
            onError = {
                redirectToLogin(errorOccurred)
            }
        )
    }

    private fun setLoadingStatus(status: Boolean) {
        if (status) {
            root.instanceInfoProgressBar.visibility = View.VISIBLE
            root.instanceInfoContent.visibility = View.INVISIBLE
            root.minecraftServerProgressBar.visibility = View.VISIBLE
            root.minecraftServerContent.visibility = View.INVISIBLE
        } else {
            root.instanceInfoProgressBar.visibility = View.GONE
            root.instanceInfoContent.visibility = View.VISIBLE
            root.minecraftServerProgressBar.visibility = View.GONE
            root.minecraftServerContent.visibility = View.VISIBLE
        }
    }

    private fun setInstanceInfoValues(instanceInfo: InstanceInfo) {
        root.instanceInfoEndpoint.text = instanceInfo.endpoint
        root.instanceInfoIP.text = instanceInfo.ip
        root.instanceInfoStatus.text = instanceInfo.status.name
        root.instanceInfoLaunchTime.text = instanceInfo.launch_time
    }

    private fun setMinecraftServerValues(minecraftInfo: MinecraftInfo) {
        root.minecraftServerOnline.text =
            if (minecraftInfo.online) yesLabelValue else noLabelValue
        root.minecraftServerPort.text = minecraftInfo.port.toString()

        root.minecraftServerVersion.text = minecraftInfo.version ?: notAvailableLabelValue
        root.minecraftServerMaxPlayers.text =
            (minecraftInfo.players?.max ?: notAvailableLabelValue).toString()
        root.minecraftServerOnlinePlayers.text =
            (minecraftInfo.players?.online ?: notAvailableLabelValue).toString()
        root.minecraftServerPlayerList.text =
            (minecraftInfo.players?.list ?: noneLabelValue).toString()
    }

    private fun showButtonsForStatus(status: InstanceStatus) {
        when (status) {
            InstanceStatus.stopping, InstanceStatus.stopped -> {
                root.openButton.visibility = View.VISIBLE
                root.closeButton.visibility = View.GONE
                root.rebootButton.visibility = View.GONE
            }
            InstanceStatus.pending, InstanceStatus.starting -> {
                root.openButton.visibility = View.GONE
                root.closeButton.visibility = View.VISIBLE
                root.rebootButton.visibility = View.GONE
            }
            InstanceStatus.running -> {
                root.openButton.visibility = View.GONE
                root.closeButton.visibility = View.VISIBLE
                root.rebootButton.visibility = View.VISIBLE
            }
        }
    }

    private fun sendServerAction(actionType: ServerAction) {
        Toast.makeText(
            context,
            requireContext().getString(R.string.sending_server_action, actionType.action),
            Toast.LENGTH_SHORT
        ).show()
        setButtonsState(false)
        ServerController.sendServerAction(actionType,
            onSuccess = {
                CooldownManager.initCooldown(
                    onStart = {
                        root.cooldownProgressBar.progress = 0
                    },
                    onFinish = {
                        root.cooldownProgressBar.progress = 100
                        setButtonsState(true)
                    },
                    onTick = { percentage ->
                        root.cooldownProgressBar.progress = percentage
                    }
                )
                getSeverStatus()
            },
            onBadCredentials = {
                redirectToLogin(expiredSession)
            },
            onError = {
                redirectToLogin(errorOccurred)
            }
        )
    }

    private fun setButtonsState(state: Boolean) {
        closeButton.setState(state)
        openButton.setState(state)
        rebootButton.setState(state)
    }

    private fun redirectToLogin(toastMessage: String?) {
        toastMessage.let {
            Toast.makeText(root.context, toastMessage, Toast.LENGTH_SHORT).show()
        }
        (activity as HomeActivity).logout(false)
    }
}