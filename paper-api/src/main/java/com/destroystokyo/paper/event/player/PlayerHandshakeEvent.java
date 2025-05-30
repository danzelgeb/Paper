package com.destroystokyo.paper.event.player;

import com.google.common.base.Preconditions;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This event is fired during a player handshake.
 * <p>
 * If there are no listeners listening to this event, the logic default
 * to your server platform will be run.
 *
 * <p>WARNING: TAMPERING WITH THIS EVENT CAN BE DANGEROUS</p>
 */
@NullMarked
public class PlayerHandshakeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String originalHandshake;
    private final String originalSocketAddressHostname;
    private @Nullable String serverHostname;
    private @Nullable String socketAddressHostname;
    private @Nullable UUID uniqueId;
    private @Nullable String propertiesJson;
    private boolean failed;
    private Component failMessage = Component.text("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!", NamedTextColor.YELLOW);

    private boolean cancelled;

    @Deprecated
    @ApiStatus.Internal
    public PlayerHandshakeEvent(final String originalHandshake, final boolean cancelled) {
        this(originalHandshake, "127.0.0.1", cancelled);
    }

    @ApiStatus.Internal
    public PlayerHandshakeEvent(final String originalHandshake, final String originalSocketAddressHostname, final boolean cancelled) {
        super(true);
        this.originalHandshake = originalHandshake;
        this.originalSocketAddressHostname = originalSocketAddressHostname;
        this.cancelled = cancelled;
    }

    /**
     * Gets the original handshake string.
     *
     * @return the original handshake string
     */
    public String getOriginalHandshake() {
        return this.originalHandshake;
    }

    /**
     * Gets the original socket address hostname.
     *
     * <p>This does not include the port.</p>
     * <p>In cases where this event is manually fired and the plugin wasn't updated yet, the default is {@code "127.0.0.1"}.</p>
     *
     * @return the original socket address hostname
     */
    public String getOriginalSocketAddressHostname() {
        return this.originalSocketAddressHostname;
    }

    /**
     * Gets the server hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @return the server hostname string
     */
    public @Nullable String getServerHostname() {
        return this.serverHostname;
    }

    /**
     * Sets the server hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @param serverHostname the server hostname string
     */
    public void setServerHostname(final String serverHostname) {
        this.serverHostname = serverHostname;
    }

    /**
     * Gets the socket address hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @return the socket address hostname string
     */
    public @Nullable String getSocketAddressHostname() {
        return this.socketAddressHostname;
    }

    /**
     * Sets the socket address hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @param socketAddressHostname the socket address hostname string
     */
    public void setSocketAddressHostname(final String socketAddressHostname) {
        this.socketAddressHostname = socketAddressHostname;
    }

    /**
     * Gets the unique id.
     *
     * @return the unique id
     */
    public @Nullable UUID getUniqueId() {
        return this.uniqueId;
    }

    /**
     * Sets the unique id.
     *
     * @param uniqueId the unique id
     */
    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Gets the profile properties.
     *
     * <p>This should be a valid JSON string.</p>
     *
     * @return the profile properties, as JSON
     */
    public @Nullable String getPropertiesJson() {
        return this.propertiesJson;
    }

    /**
     * Determines if authentication failed.
     * <p>
     * When {@code true}, the client connecting will be disconnected
     * with the {@link #getFailMessage() fail message}.
     *
     * @return {@code true} if authentication failed, {@code false} otherwise
     */
    public boolean isFailed() {
        return this.failed;
    }

    /**
     * Sets if authentication failed and the client should be disconnected.
     * <p>
     * When {@code true}, the client connecting will be disconnected
     * with the {@link #getFailMessage() fail message}.
     *
     * @param failed {@code true} if authentication failed, {@code false} otherwise
     */
    public void setFailed(final boolean failed) {
        this.failed = failed;
    }

    /**
     * Sets the profile properties.
     *
     * <p>This should be a valid JSON string.</p>
     *
     * @param propertiesJson the profile properties, as JSON
     */
    public void setPropertiesJson(final String propertiesJson) {
        this.propertiesJson = propertiesJson;
    }

    /**
     * Gets the message to display to the client when authentication fails.
     *
     * @return the message to display to the client
     */
    public Component failMessage() {
        return this.failMessage;
    }

    /**
     * Sets the message to display to the client when authentication fails.
     *
     * @param failMessage the message to display to the client
     */
    public void failMessage(final Component failMessage) {
        this.failMessage = failMessage;
    }

    /**
     * Gets the message to display to the client when authentication fails.
     *
     * @return the message to display to the client
     * @deprecated use {@link #failMessage()}
     */
    @Deprecated
    public String getFailMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.failMessage());
    }

    /**
     * Sets the message to display to the client when authentication fails.
     *
     * @param failMessage the message to display to the client
     * @deprecated use {@link #failMessage(Component)}
     */
    @Deprecated
    public void setFailMessage(final String failMessage) {
        Preconditions.checkArgument(failMessage != null && !failMessage.isEmpty(), "fail message cannot be null or empty");
        this.failMessage(LegacyComponentSerializer.legacySection().deserialize(failMessage));
    }

    /**
     * Determines if this event is cancelled.
     * <p>
     * When this event is cancelled, custom handshake logic will not
     * be processed.
     *
     * @return {@code true} if this event is cancelled, {@code false} otherwise
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets if this event is cancelled.
     * <p>
     * When this event is cancelled, custom handshake logic will not
     * be processed.
     *
     * @param cancel {@code true} if this event is cancelled, {@code false} otherwise
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
