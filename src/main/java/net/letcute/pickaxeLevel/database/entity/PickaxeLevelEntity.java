package net.letcute.pickaxeLevel.database.entity;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickaxeLevelEntity {

    private int id;
    private String name;
    private int level;
    private int exp;
}
