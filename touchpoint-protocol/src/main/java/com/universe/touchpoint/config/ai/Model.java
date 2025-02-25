package com.universe.touchpoint.config.ai;

public enum Model {

    GPT_3_5("gpt-3.5"),
    GPT_4("gpt-4"),
    o1("o1"),
    ClAUDE_3_5_SONNET("claudan-3.5"),
    DINO_V2("facebook/dinov2-base"),
    SIGLIP("google/siglip-base-patch16-224"),
    NONE("");

    private final String name;

    Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
