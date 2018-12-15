// NOTE This code is taken from the SC Quark BandSplitter to save users installing the Quark separately

TXBandSplitter : Filter {
	*lowpass {
		|sig, f, order=2|
		if (order == 2) {
			^LPF.ar(LPF.ar(sig, f), f)
		} {
			^BLPF.ar(BLPF.ar(sig, order, f), order, f)
		}
	}

	*highpass {
		|sig, f, order=2|
		if (order == 2) {
			^HPF.ar(HPF.ar(sig, f), f)
		} {
			^BHPF.ar(BHPF.ar(sig, order, f), order, f)
		}

	}

	*allpass {
		|sig, f, order=2|
		^(TXBandSplitter.highpass(sig, f, order) + TXBandSplitter.lowpass(sig, f, order))
	}
}

TXBandSplitter2 : TXBandSplitter {
	*ar {
		|sig, freq, order=2|
		var a, b;

		//  0hz freq nyquist
		//  |  a  |  b  |

		a = this.lowpass(sig, freq);
		b = this.highpass(sig, freq);
		^[a, b];
	}
}

TXBandSplitter4 : TXBandSplitter {
	*ar {
		|sig, f1, f2, f3, order=2|
		var low, high, a, b, c, d;

		//  0hz  f1    f2    f3   nyquist
		//  |  a  |  b  |  c  |  d  |

		#low, high = TXBandSplitter2.ar(sig, f2);

		a = this.lowpass(low, f1);
		b = this.highpass(low, f1);

		c = this.lowpass(high, f3);
		d = this.highpass(high, f3);

		// Align the phase of the top half with the phase of the bottom half.
		c = this.allpass(c, f1);
		d = this.allpass(d, f1);

		a = this.allpass(a, f3);
		b = this.allpass(b, f3);

		^[a, b, c, d]
	}
}

TXBandSplitter8 : TXBandSplitter {
	*ar {
		|sig, f1, f2, f3, f4, f5, f6, f7, order=4|
		var low, high, a, b, c, d, e, f, g, h;

		//  0hz  f1    f2    f3   nyquist
		//  |  a  |  b  |  c  |  d  |

		#low, high = TXBandSplitter2.ar(sig, f4);
		#a, b, c, d = this.allpass(TXBandSplitter4.ar(low, f1, f2, f3), f6);
		#e, f, g, h = this.allpass(TXBandSplitter4.ar(high, f5, f6, f7), f2);

		^[a, b, c, d, e, f, g, h]
	}
}