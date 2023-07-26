package cn.edu.sjtu.patrickli.cryptex.model

enum class ThemePreference(val value: Int) {
    AUTO(0),
    LIGHT(1),
    DARK(2);
    companion object {
        fun fromOrdinal(ordinal: Int): ThemePreference {
            return values().first { it.ordinal == ordinal }
        }
    }
}