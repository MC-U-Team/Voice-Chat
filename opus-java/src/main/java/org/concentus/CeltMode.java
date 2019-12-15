/* Copyright (c) 2007-2008 CSIRO
   Copyright (c) 2007-2011 Xiph.Org Foundation
   Originally written by Jean-Marc Valin, Gregory Maxwell, Koen Vos,
   Timothy B. Terriberry, and the Opus open-source contributors
   Ported to Java by Logan Stromberg

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions
   are met:

   - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

   - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.

   - Neither the name of Internet Society, IETF or IETF Trust, nor the
   names of specific contributors, may be used to endorse or promote
   products derived from this software without specific prior written
   permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
   OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.concentus;

class CeltMode {

    int Fs = 0;
    int overlap = 0;

    int nbEBands = 0;
    int effEBands = 0;
    int[] preemph = {0, 0, 0, 0};

    /// <summary>
    /// Definition for each "pseudo-critical band"
    /// </summary>
    short[] eBands = null;

    int maxLM = 0;
    int nbShortMdcts = 0;
    int shortMdctSize = 0;

    /// <summary>
    /// Number of lines in allocVectors
    /// </summary>
    int nbAllocVectors = 0;

    /// <summary>
    /// Number of bits in each band for several rates
    /// </summary>
    short[] allocVectors = null;
    short[] logN = null;

    int[] window = null;
    MDCTLookup mdct = new MDCTLookup();
    PulseCache cache = new PulseCache();

    private CeltMode() {
    }

    static final CeltMode mode48000_960_120 = new CeltMode();

    static {
        mode48000_960_120.Fs = 48000;
        mode48000_960_120.overlap = 120;
        mode48000_960_120.nbEBands = 21;
        mode48000_960_120.effEBands = 21;
        mode48000_960_120.preemph = new int[]{27853, 0, 4096, 8192};
        mode48000_960_120.eBands = CeltTables.eband5ms;
        mode48000_960_120.maxLM = 3;
        mode48000_960_120.nbShortMdcts = 8;
        mode48000_960_120.shortMdctSize = 120;
        mode48000_960_120.nbAllocVectors = 11;
        mode48000_960_120.allocVectors = CeltTables.band_allocation;
        mode48000_960_120.logN = CeltTables.logN400;
        mode48000_960_120.window = CeltTables.window120;
        mode48000_960_120.mdct = new MDCTLookup();

        mode48000_960_120.mdct.n = 1920;
        mode48000_960_120.mdct.maxshift = 3;
        mode48000_960_120.mdct.kfft = new FFTState[]{
            CeltTables.fft_state48000_960_0,
            CeltTables.fft_state48000_960_1,
            CeltTables.fft_state48000_960_2,
            CeltTables.fft_state48000_960_3,};

        mode48000_960_120.mdct.trig = CeltTables.mdct_twiddles960;
        mode48000_960_120.cache = new PulseCache();
        mode48000_960_120.cache.size = 392;
        mode48000_960_120.cache.index = CeltTables.cache_index50;
        mode48000_960_120.cache.bits = CeltTables.cache_bits50;
        mode48000_960_120.cache.caps = CeltTables.cache_caps50;
    }
;
}
