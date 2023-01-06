package me.jabberjerry.motion.util;

public enum Events {
    AsyncPlayerPreLoginEvent, //Stores details for players attempting to log in.
    AsyncTabCompleteEvent, //Allows plugins to compute tab completion results asynchronously. If this event provides completions, then the standard synchronous process will not be fired to populate the results. However, the synchronous TabCompleteEvent will fire with the Async results. Only 1 process will be allowed to provide completions, the Async Event, or the standard process.
    FillProfileEvent, //Fired once a profiles additional properties (such as textures) has been filled
    GS4QueryEvent, //This event is fired if server is getting queried over GS4 Query protocol Adapted from Velocity's ProxyQueryEvent
    InventoryMoveItemEvent, //Called when some entity or block (e.g. hopper) tries to move items directly from one inventory to another. When this event is called, the initiator may already have removed the item from the source inventory and is ready to move it into the destination inventory. If this event is cancelled, the items will be returned to the source inventory, if needed. If this event is not cancelled, the initiator will try to put the ItemStack into the destination inventory. If this is not possible and the ItemStack has not been modified, the source inventory slot will be restored to its former state. Otherwise any additional items will be discarded.
    InventoryPickupItemEvent, //Called when a hopper or hopper minecart picks up a dropped item.
    LookupProfileEvent, //Allows a plugin to be notified anytime AFTER a Profile has been looked up from the Mojang API This is an opportunity to view the response and potentially cache things. No guarantees are made about thread execution context for this event. If you need to know, check event.isAsync()
    PlayerConnectionCloseEvent, //see docs
    PlayerHandshakeEvent, //see docs
    PlayerLeashEntityEvent, //Called immediately prior to a creature being leashed by a player.
    PlayerPreLoginEvent, //Stores details for players attempting to log in
    PreCreatureSpawnEvent, //see docs
    PreFillProfileEvent, //ired when the server is requesting to fill in properties of an incomplete profile, such as textures. Allows plugins to pre populate cached properties and avoid a call to the Mojang API
    PreLookupProfileEvent,//Allows a plugin to intercept a Profile Lookup for a Profile by name At the point of event fire, the UUID and properties are unset. If a plugin sets the UUID, and optionally the properties, the API call to look up the profile may be skipped. No guarantees are made about thread execution context for this event. If you need to know, check event.isAsync()
    ProfileWhitelistVerifyEvent, //Fires when the server needs to verify if a player is whitelisted. Plugins may override/control the servers whitelist with this event, and dynamically change the kick message.
    ServerExceptionEvent, //Called whenever an exception is thrown in a recoverable section of the server.
    ServerTickEndEvent, //Called when the server has finished ticking the main loop
    ServerTickStartEvent, //?? Presumably called when the server is about to stat ticking the main loop
    TabCompleteEvent, //see docs
    UnknownCommandEvent, //Thrown when a player executes a command that is not defined
    WhitelistToggleEvent, //This event is fired when whitelist is toggled
}

enum BlockEvents {
    BeaconEffectEvent,
    BlockBurnEvent,
    BlockCanBuildEvent,
    BlockCookEvent,
    BlockDamageEvent,
    BlockDestroyEvent,
    BlockDispenseEvent,
    BlockDropItemEvent,
    BlockExpEvent,
    BlockExplodeEvent,
    BlockFadeEvent,
    BlockFertilizeEvent,
    BlockFromToEvent,
    BlockGrowEvent,
    BlockIgniteEvent,
    BlockPhysicsEvent,
    BlockPistonEvent,
    BlockPlaceEvent,
    BlockRedstoneEvent,
    BlockShearEntityEvent,
    BrewEvent,
    BrewingStandFuelEvent,
    CauldronLevelChangeEvent,
    FluidLevelChangeEvent,
    FurnaceBurnEvent,
    LeavesDecayEvent,
    MoistureChangeEvent,
    NotePlayEvent,
    SignChangeEvent,
    SpongeAbsorbEvent,
    TNTPrimeEvent
}

enum EntityEvents {
    AreaEffectCloudApplyEvent,
    BatToggleSleepEvent,
    CreeperIgniteEvent,
    CreeperPowerEvent,
    EnderDragonChangePhaseEvent,
    EnderDragonFireballHitEvent,
    EnderDragonFlameEvent,
    EnderDragonShootFireballEvent,
    EndermanAttackPlayerEvent,
    EndermanEscapeEvent,
    EntityAddToWorldEvent,
    EntityAirChangeEvent,
    EntityBreedEvent,
    EntityChangeBlockEvent,
    EntityCombustEvent,
    EntityCreatePortalEvent,
    EntityDamageEvent,
    EntityDeathEvent,
    EntityDismountEvent,
    EntityDropItemEvent,
    EntityExplodeEvent,
    EntityInteractEvent,
    EntityKnockbackByEntityEvent,
    EntityMountEvent,
    EntityPathfindEvent,
    EntityPickupItemEvent,
    EntityPlaceEvent,
    EntityPortalEnterEvent,
    EntityPoseChangeEvent,
    EntityPotionEffectEvent,
    EntityRegainHealthEvent,
    EntityRemoveFromWorldEvent,
    EntityResurrectEvent,
    EntityShootBowEvent,
    EntitySpawnEvent,
    EntityTameEvent,
    EntityTargetEvent,
    EntityTeleportEvent,
    EntityToggleGlideEvent,
    EntityToggleSwimEvent,
    EntityTransformedEvent,
    EntityTransformEvent,
    EntityUnleashEvent,
    ExperienceOrbMergeEvent,
    ExplosionPrimeEvent,
    FireworkExplodeEvent,
    FoodLevelChangeEvent,
    HorseJumpEvent,
    ItemDespawnEvent,
    ItemMergeEvent,
    PigZombieAngerEvent,
    ProjectileCollideEvent,
    ProjectileHitEvent,
    SheepDyeWoolEvent,
    SheepRegrowWoolEvent,
    SkeletonHorseTrapEvent,
    SlimePathfindEvent,
    SlimeSplitEvent,
    TurtleGoHomeEvent,
    TurtleLayEggEvent,
    TurtleStartDiggingEvent,
    VillagerAcquireTradeEvent,
    VillagerCareerChangeEvent,
    VillagerReplenishTradeEvent,
    WitchConsumePotionEvent,
    WitchReadyPotionEvent,
    WitchThrowPotionEvent
}

enum HangingEvents {
    HangingBreakEvent,
    HangingPlaceEvent
}

enum InventoryEvents {
    AnvilDamagedEvent,
    EnchantItemEvent,
    InventoryCloseEvent,
    InventoryInteractEvent,
    InventoryOpenEvent,
    PrepareAnvilEvent,
    PrepareItemCraftEvent,
    PrepareItemEnchantEvent
}

enum PlayerEvents {
    AsyncPlayerChatEvent,
    IllegalPacketEvent,
    LootableInventoryReplenishEvent,
    PlayerAchievementAwardedEvent,
    PlayerAdvancementCriterionGrantEvent,
    PlayerAdvancementDoneEvent,
    PlayerAnimationEvent,
    PlayerArmorChangeEvent,
    PlayerAttemptPickupItemEvent,
    PlayerBedEnterEvent,
    PlayerBedLeaveEvent,
    PlayerBucketEvent,
    PlayerChangedMainHandEvent,
    PlayerChangedWorldEvent,
    PlayerChannelEvent,
    PlayerChatEvent,
    PlayerChatTabCompleteEvent,
    PlayerCommandPreprocessEvent,
    PlayerCommandSendEvent,
    PlayerDropItemEvent,
    PlayerEditBookEvent,
    PlayerEggThrowEvent,
    PlayerElytraBoostEvent,
    PlayerExpChangeEvent,
    PlayerFishEvent,
    PlayerGameModeChangeEvent,
    PlayerInitialSpawnEvent,
    PlayerInteractEntityEvent,
    PlayerInteractEvent,
    PlayerItemBreakEvent,
    PlayerItemConsumeEvent,
    PlayerItemDamageEvent,
    PlayerItemHeldEvent,
    PlayerItemMendEvent,
    PlayerJoinEvent,
    PlayerJumpEvent,
    PlayerKickEvent,
    PlayerLaunchProjectileEvent,
    PlayerLevelChangeEvent,
    PlayerLocaleChangeEvent,
    PlayerLoginEvent,
    PlayerMoveEvent,
    PlayerNaturallySpawnCreaturesEvent,
    PlayerPickupExperienceEvent,
    PlayerPickupItemEvent,
    PlayerPostRespawnEvent,
    PlayerQuitEvent,
    PlayerReadyArrowEvent,
    PlayerRecipeDiscoverEvent,
    PlayerResourcePackStatusEvent,
    PlayerRespawnEvent,
    PlayerRiptideEvent,
    PlayerShearEntityEvent,
    PlayerSpawnLocationEvent,
    PlayerStartSpectatingEntityEvent,
    PlayerStatisticIncrementEvent,
    PlayerStopSpectatingEntityEvent,
    PlayerSwapHandItemsEvent,
    PlayerTakeLecternBookEvent,
    PlayerToggleFlightEvent,
    PlayerToggleSneakEvent,
    PlayerToggleSprintEvent,
    PlayerUseUnknownEntityEvent,
    PlayerVelocityEvent
}

enum ServerEvents {
    BroadcastMessageEvent,
    MapInitializeEvent,
    PluginEvent,
    ServerCommandEvent,
    ServerListPingEvent,
    ServerLoadEvent,
    ServiceEvent
}

enum VehicleEvents {
    VehicleCollisionEvent,
    VehicleCreateEvent,
    VehicleDamageEvent,
    VehicleDestroyEvent,
    VehicleEnterEvent,
    VehicleExitEvent,
    VehicleMoveEvent,
    VehicleUpdateEvent
}

enum WeatherEvents {
    LightningStrikeEvent,
    ThunderChangeEvent,
    WeatherChangeEvent
}

enum WorldEvents {
    PortalCreateEvent,
    SpawnChangeEvent,
    StructureGrowEvent,
    WorldInitEvent,
    WorldLoadEvent,
    WorldSaveEvent,
    WorldUnloadEvent
}

enum RaidEvents {
    RaidFinishEvent,
    RaidSpawnWaveEvent,
    RaidStopEvent,
    RaidTriggerEvent
}

enum ChunkEvents {
    ChunkLoadEvent,
    ChunkPopulateEvent,
    ChunkUnloadEvent
}





enum PlayerEventsTemp {
    PlayerLeashEntityEvent,
    PlayerAchievementAwardedEvent,
    PlayerAdvancementCriterionGrantEvent,
    PlayerAdvancementDoneEvent,
    PlayerAnimationEvent,
    PlayerArmorChangeEvent,
    PlayerAttemptPickupItemEvent,
    PlayerBedEnterEvent,
    PlayerBedLeaveEvent,
    PlayerBucketEvent,
    PlayerChangedMainHandEvent,
    PlayerChangedWorldEvent,
    PlayerChatEvent,
    PlayerDropItemEvent,
    PlayerEditBookEvent,
    PlayerEggThrowEvent,
    PlayerElytraBoostEvent,
    PlayerExpChangeEvent,
    PlayerFishEvent,
    PlayerGameModeChangeEvent,
    PlayerInitialSpawnEvent,
    PlayerInteractEntityEvent,
    PlayerInteractEvent,
    PlayerItemBreakEvent,
    PlayerItemConsumeEvent,
    PlayerItemDamageEvent,
    PlayerItemHeldEvent,
    PlayerItemMendEvent,
    PlayerJoinEvent,
    PlayerJumpEvent,
    PlayerKickEvent,
    PlayerLaunchProjectileEvent,
    PlayerLevelChangeEvent,
    PlayerLoginEvent,
    PlayerMoveEvent,
    PlayerNaturallySpawnCreaturesEvent,
    PlayerPickupExperienceEvent,
    PlayerPickupItemEvent,
    PlayerPostRespawnEvent,
    PlayerQuitEvent,
    PlayerReadyArrowEvent,
    PlayerRecipeDiscoverEvent,
    PlayerRespawnEvent,
    PlayerRiptideEvent,
    PlayerShearEntityEvent,
    PlayerSpawnLocationEvent,
    PlayerStartSpectatingEntityEvent,
    PlayerStatisticIncrementEvent,
    PlayerStopSpectatingEntityEvent,
    PlayerSwapHandItemsEvent,
    PlayerTakeLecternBookEvent,
    PlayerToggleFlightEvent,
    PlayerToggleSneakEvent,
    PlayerToggleSprintEvent,
    PlayerVelocityEvent
}