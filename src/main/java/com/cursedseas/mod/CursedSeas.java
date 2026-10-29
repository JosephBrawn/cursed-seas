package com.cursedseas.mod;

import com.cursedseas.mod.registry.ModItems;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(CursedSeas.MODID)
public class CursedSeas {

    // Уникальный идентификатор нашего мода
    public static final String MODID = "cursedseas";

    // Инструмент для записи сообщений в журнал
    public static final Logger LOGGER = LogUtils.getLogger();

    // Конструктор главного класса мода
    public CursedSeas(IEventBus modEventBus, ModContainer modContainer) {

        // Подключаем регистрацию предметов
        ModItems.ITEMS.register(modEventBus);
    }
}
