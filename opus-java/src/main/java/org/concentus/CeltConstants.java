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

class CeltConstants {

    public static final int Q15ONE = 32767;

    public static final float CELT_SIG_SCALE = 32768.0f;

    public static final int SIG_SHIFT = 12;

    public static final int NORM_SCALING = 16384;

    public static final int DB_SHIFT = 10;

    public static final int EPSILON = 1;
    public static final int VERY_SMALL = 0;
    public static final short VERY_LARGE16 = ((short) 32767);
    public static final short Q15_ONE = ((short) 32767);

    public static final int COMBFILTER_MAXPERIOD = 1024;
    public static final int COMBFILTER_MINPERIOD = 15;

    // from opus_decode.c
    public static final int DECODE_BUFFER_SIZE = 2048;

    // from modes.c
    /* Alternate tuning (partially derived from Vorbis) */
    public static final int BITALLOC_SIZE = 11;
    public static final int MAX_PERIOD = 1024;

    // from static_modes_float.h
    public static final int TOTAL_MODES = 1;

    // from rate.h
    public static final int MAX_PSEUDO = 40;
    public static final int LOG_MAX_PSEUDO = 6;

    public static final int CELT_MAX_PULSES = 128;

    public static final int MAX_FINE_BITS = 8;

    public static final int FINE_OFFSET = 21;
    public static final int QTHETA_OFFSET = 4;
    public static final int QTHETA_OFFSET_TWOPHASE = 16;

    /* The maximum pitch lag to allow in the pitch-based PLC. It's possible to save
CPU time in the PLC pitch search by making this smaller than MAX_PERIOD. The
current value corresponds to a pitch of 66.67 Hz. */
    public static final int PLC_PITCH_LAG_MAX = 720;

    /* The minimum pitch lag to allow in the pitch-based PLC. This corresponds to a
       pitch of 480 Hz. */
    public static final int PLC_PITCH_LAG_MIN = 100;

    public static final int LPC_ORDER = 24;
}
