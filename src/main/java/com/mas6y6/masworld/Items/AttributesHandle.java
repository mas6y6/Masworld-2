package com.mas6y6.masworld.Items;

import com.mas6y6.masworld.Items.Attributes.*;
import com.mas6y6.masworld.Masworld;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class AttributesHandle {
    public Items items;

    public LiteralArgumentBuilder<CommandSourceStack> commands = Commands.literal("attributes");

    public SpecialEffect specialEffect;

    public DynamitePower dynamitePower;
    public DynamiteFuse dynamiteFuse;

    public WeaponDamage weaponDamage;
    public WeaponCooldown weaponCooldown;

    public ShulkerSwordCooldown shulkerSwordCooldown;
    public ShulkerSwordBullet shulkerSwordBullet;
    public ShulkerSwordRange shulkerSwordRange;

    public EvokerBookCooldown evokerBookCooldown;
    public EvokerBookSpacing evokerBookSpacing;
    public EvokerBookRange evokerBookRange;
    public EvokerBookAngle evokerBookAngle;
    public EvokerBookBeamCount evokerBookBeamCount;

    public AttributesHandle() {
        this.specialEffect = new SpecialEffect(Masworld.instance());

        this.dynamitePower = new DynamitePower(Masworld.instance());
        this.dynamiteFuse = new DynamiteFuse(Masworld.instance());

        this.weaponDamage = new WeaponDamage(Masworld.instance());
        this.weaponCooldown = new WeaponCooldown(Masworld.instance());

        this.shulkerSwordCooldown = new ShulkerSwordCooldown(Masworld.instance());
        this.shulkerSwordBullet = new ShulkerSwordBullet(Masworld.instance());
        this.shulkerSwordRange = new ShulkerSwordRange(Masworld.instance());

        this.evokerBookCooldown = new EvokerBookCooldown(Masworld.instance());
        this.evokerBookSpacing = new EvokerBookSpacing(Masworld.instance());
        this.evokerBookRange = new EvokerBookRange(Masworld.instance());
        this.evokerBookAngle = new EvokerBookAngle(Masworld.instance());
        this.evokerBookBeamCount = new EvokerBookBeamCount(Masworld.instance());
    }

    public LiteralArgumentBuilder<CommandSourceStack> buildCommands() {
        commands.then(Commands.literal("special_effect")
                .then(
                        Commands.literal("get").executes(specialEffect::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", StringArgumentType.word())
                                        .executes(specialEffect::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(specialEffect::reset)
                )
        );

        commands.then(Commands.literal("dynamite_power")
                .then(
                        Commands.literal("get").executes(dynamitePower::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(1.0f,255.f))
                                        .executes(dynamitePower::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(dynamitePower::reset)
                )
        );

        commands.then(Commands.literal("shulker_sword_cooldown")
                .then(
                        Commands.literal("get").executes(shulkerSwordCooldown::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", LongArgumentType.longArg())
                                        .executes(shulkerSwordCooldown::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(shulkerSwordCooldown::reset)
                )
        );

        commands.then(Commands.literal("shulker_sword_range")
                .then(
                        Commands.literal("get").executes(shulkerSwordRange::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0,255))
                                        .executes(shulkerSwordRange::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(shulkerSwordRange::reset)
                )
        );

        commands.then(Commands.literal("shulker_sword_bullet")
                .then(
                        Commands.literal("get").executes(shulkerSwordBullet::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                        .executes(shulkerSwordBullet::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(shulkerSwordBullet::reset)
                )
        );

        commands.then(Commands.literal("dynamite_fuse")
                .then(
                        Commands.literal("get").executes(dynamiteFuse::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", LongArgumentType.longArg())
                                        .executes(dynamiteFuse::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(dynamiteFuse::reset)
                )
        );

        commands.then(Commands.literal("weapon_damage")
                .then(
                        Commands.literal("get").executes(weaponDamage::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                        .executes(weaponDamage::change)
                                )
                )
                .then(
                        Commands.literal("reset").executes(weaponDamage::reset)
                )
        );

        commands.then(Commands.literal("evoker_book_cooldown")
                .then(
                        Commands.literal("get").executes(evokerBookCooldown::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", LongArgumentType.longArg())
                                        .executes(evokerBookCooldown::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(evokerBookCooldown::reset)
                )
        );

        commands.then(Commands.literal("evoker_book_range")
                .then(
                        Commands.literal("get").executes(evokerBookRange::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                        .executes(evokerBookRange::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(evokerBookRange::reset)
                )
        );

        commands.then(Commands.literal("evoker_book_spacing")
                .then(
                        Commands.literal("get").executes(evokerBookSpacing::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                        .executes(evokerBookSpacing::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(evokerBookSpacing::reset)
                )
        );

        commands.then(Commands.literal("evoker_book_angle")
                .then(
                        Commands.literal("get").executes(evokerBookAngle::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                        .executes(evokerBookAngle::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(evokerBookAngle::reset)
                )
        );

        commands.then(Commands.literal("evoker_book_beamcount")
                .then(
                        Commands.literal("get").executes(evokerBookBeamCount::get)
                )
                .then(
                        Commands.literal("change")
                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                        .executes(evokerBookBeamCount::set)
                                )
                )
                .then(
                        Commands.literal("reset").executes(evokerBookBeamCount::reset)
                )
        );

        return commands;
    }
}
