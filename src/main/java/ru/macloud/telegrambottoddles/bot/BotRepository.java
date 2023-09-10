package ru.macloud.telegrambottoddles.bot;

import org.springframework.data.repository.CrudRepository;

interface BotRepository extends CrudRepository<BotConfig, Integer> { }
