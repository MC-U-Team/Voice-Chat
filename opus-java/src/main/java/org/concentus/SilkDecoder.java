/* Copyright (c) 2006-2011 Skype Limited. All Rights Reserved
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

/// <summary>
/// Decoder super struct
/// </summary>
class SilkDecoder {

    final SilkChannelDecoder[] channel_state = new SilkChannelDecoder[SilkConstants.DECODER_NUM_CHANNELS];
    final StereoDecodeState sStereo = new StereoDecodeState();
    int nChannelsAPI = 0;
    int nChannelsInternal = 0;
    int prev_decode_only_middle = 0;

    SilkDecoder() {
        for (int c = 0; c < SilkConstants.DECODER_NUM_CHANNELS; c++) {
            channel_state[c] = new SilkChannelDecoder();
        }
    }

    void Reset() {
        for (int c = 0; c < SilkConstants.DECODER_NUM_CHANNELS; c++) {
            channel_state[c].Reset();
        }
        sStereo.Reset();
        nChannelsAPI = 0;
        nChannelsInternal = 0;
        prev_decode_only_middle = 0;
    }
}
