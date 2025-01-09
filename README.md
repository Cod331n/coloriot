# Coloriot
Простая библиотека, которая позволяет удобно работать с партиклами в майнкрафт.
Отправка пакетов и оптимизация уже сделана под капотом, остается только применять готовые решения.

## Установка 
Для того чтобы использовать эту библиотеку в своем проекте, необходимо добавить её зависимость в `build.gradle` или `build.gradle.kts`.
```kotlin
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation("com.github.Cod331n:coloriot:v1.0.0")
}
```

## Использование
В классе плагина, передайте его объект в класс Tasks `Tasks.setJavaPlugin(this);` после чего можно использовать все функции библиотеки.

Основной класс `ParticleManager`, передаем туда значения, которые нам нужны, затем выбираем стратегию появления партиклов и готово.
Вот пример кода:
```java
ParticleManager particleManager = new ParticleManager();
particleManager.randomSphere(new ParticleRequest(Particle.VILLAGER_HAPPY), location.clone().subtract(0, -1, 0), 1, 6, true).once(player, 0);
```

Также партиклы можно комбинировать в один пакет, чтобы не нагружать игрока отправкой пакетов.
В примере показано, как отправлять каждую секунду игроку комбинированный пакет партиклов в виде 4 сфер, в сумме 160 партиклов.
```java
ParticleManager particleManager = new ParticleManager();
Tasks.every(20, task -> {
    particleManager.combine(
            particleManager.intervalSphere(new ParticleRequest(Particle.BARRIER), player.getLocation().clone().subtract(1, -1, 1), 2, 40, true),
            particleManager.intervalSphere(new ParticleRequest(Particle.BARRIER), player.getLocation().clone().subtract(-1, -1, -1), 2, 40, true),
            particleManager.intervalSphere(new ParticleRequest(Particle.BARRIER), player.getLocation().clone().subtract(0, -3, 0), 1.5, 40, true),
            particleManager.intervalSphere(new ParticleRequest(Particle.BARRIER), player.getLocation().clone().subtract(0, -5, 0), 1.6, 40, true)
    ).once(player, 0);
});
```
