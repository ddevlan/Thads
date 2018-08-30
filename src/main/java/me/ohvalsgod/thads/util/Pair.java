package me.ohvalsgod.thads.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pair<K, V> {

    private K key;
    private V value;

}
