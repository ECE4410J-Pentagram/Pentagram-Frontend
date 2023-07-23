package cn.edu.sjtu.patrickli.cryptex.model

enum class QrCodeScanState(val state: Int) {
    PEND(0),
    SUCCESS(1),
    FAIL(2)
}