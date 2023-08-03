package cn.edu.sjtu.patrickli.cryptex.model.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.security.PrivateKey
import java.util.Random
import kotlin.math.roundToInt

object Shuffle {

//  Note: use LinkedHashSet to maintain insertion order
//  Return a random sequence (without repeating)
//  length:         length of the sequence
//  offset:         offset the whole sequence (originally 0)
//  seed:           the random seed
    fun doFinal(length: Int, bound: Int, offset: Int, seed: Int): MutableSet<Int> {
        val rng = Random()
        rng.setSeed(seed.toLong())

        val shuffleSeq: MutableSet<Int> = LinkedHashSet()
        while (shuffleSeq.size < length) {
            val next = rng.nextInt(bound - offset)
            shuffleSeq.add(next + offset) // As we're adding to a set, this will automatically do a containment check
        }
        return shuffleSeq
    }

}