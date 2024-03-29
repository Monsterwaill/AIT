package com.mdt.ait.core.init;

import com.mdt.ait.AIT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AITSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AIT.MOD_ID);

    public static final RegistryObject<SoundEvent> SONIC_SOUND = SOUNDS.register("sonic_sound", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID,"sonic_sound")));
    public static final RegistryObject<SoundEvent> SONIC_ACTIVE = SOUNDS.register("sonic_active", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "sonic_active")));
    public static final RegistryObject<SoundEvent> POLICE_BOX_OPEN = SOUNDS.register("police_box_open", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID,"police_box_open")));
    public static final RegistryObject<SoundEvent> POLICE_BOX_CLOSE = SOUNDS.register("police_box_close", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID,"police_box_close")));
    public static final RegistryObject<SoundEvent> TARDIS_FIRST_ENTER = SOUNDS.register("tardis_first_enter", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "tardis_first_enter")));
    public static final RegistryObject<SoundEvent> TYPEWRITER_DING = SOUNDS.register("typewriter_ding", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "typewriter_ding")));
    public static final RegistryObject<SoundEvent> TARDIS_TAKEOFF = SOUNDS.register("tardis_takeoff", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "tardis_takeoff")));
    public static final RegistryObject<SoundEvent> TARDIS_LANDING = SOUNDS.register("tardis_landing", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "tardis_landing")));
    public static final RegistryObject<SoundEvent> TARDIS_FAIL_LANDING = SOUNDS.register("tardis_fail_landing", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "tardis_fail_landing")));
    public static final RegistryObject<SoundEvent> CLOISTER_BELL = SOUNDS.register("cloister_bell", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "cloister_bell")));
    public static final RegistryObject<SoundEvent> TARDIS_LOCK = SOUNDS.register("tardis_lock", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "tardis_lock")));
    public static final RegistryObject<SoundEvent> KNINE_GROWL = SOUNDS.register("knine_growl", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "knine_growl")));
    public static final RegistryObject<SoundEvent> KNINE_WHINE = SOUNDS.register("knine_whine", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "knine_whine")));
    public static final RegistryObject<SoundEvent> KNINE_PANT = SOUNDS.register("knine_pant", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "knine_pant")));
    public static final RegistryObject<SoundEvent> KNINE_HURT = SOUNDS.register("knine_hurt", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "knine_hurt")));
    public static final RegistryObject<SoundEvent> KNINE_DEATH = SOUNDS.register("knine_death", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "knine_death")));
    public static final RegistryObject<SoundEvent> KNINE_AMBIENT = SOUNDS.register("knine_ambient", () -> new SoundEvent(
            new ResourceLocation(AIT.MOD_ID, "knine_ambient")));
    public static final RegistryObject<SoundEvent> FLY_WING_SOUND = SOUNDS.register("fly_wing_sound", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "fly_wing_sound")));
    public static final RegistryObject<SoundEvent> CYBER_SHOCK_SOUND = SOUNDS.register("cyber_shock_sound", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "cyber_shock_sound")));
    public static final RegistryObject<SoundEvent> BUTTON_PRESS = SOUNDS.register("button_press", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "button_press")));
    public static final RegistryObject<SoundEvent> CYBER_MARCH_QUICK = SOUNDS.register("cyber_march_quick", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "cyber_march_quick")));
    public static final RegistryObject<SoundEvent> CYBER_YWBD = SOUNDS.register("cyber_ywbd", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "cyber_ywbd")));
    public static final RegistryObject<SoundEvent> DALEK_DEATH = SOUNDS.register("dalek_death", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "dalek_death")));
    public static final RegistryObject<SoundEvent> DALEK_MOVE = SOUNDS.register("dalek_move", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "dalek_move")));
    public static final RegistryObject<SoundEvent> DALEK_EXTERMINATE = SOUNDS.register("dalek_exterminate", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "dalek_exterminate")));
    public static final RegistryObject<SoundEvent> DALEK_GUNFIRE = SOUNDS.register("dalek_gunfire", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "dalek_gunfire")));
    public static final RegistryObject<SoundEvent> DALEK_HIT = SOUNDS.register("dalek_hit", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "dalek_hit")));
    public static final RegistryObject<SoundEvent> ROUNDEL_DOORS = SOUNDS.register("roundel_doors", () -> new SoundEvent (
            new ResourceLocation(AIT.MOD_ID, "roundel_doors")));
}
