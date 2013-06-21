// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

/*
// N.B. THIS IS INCOMPLETE - MAY NOT BE NEEDED

// TEST CODE:
(
w = Window("My Analyzer", Rect(0, 0, 511, 300)); // width should be 511
f = FreqScopeView(w, w.view.bounds);
f.freqMode_(1); // change to log scale so we can see them
f.inBus_(1); // look at bus 1
f.dbRange_(200); // expand amplitude range
f.active_(true); // turn it on the first time;

w.onClose_({ f.kill }); // you must have this
w.front;
{ 0.1 * SinOsc.ar([500, 1000], 0, 0.25).mean.dup }.play(s); // start two sine waves
)

f.active_(false); // turn scope off (watch CPU)
f.active_(true); // turn it back on

*/

TXFreqScope {
	classvar dataEvent;

	*initClass{
		dataEvent = ();
		dataEvent.keepOnTop = false
	}

	*open{ arg winColour= TXColour(0.6,0.8,0.8), inLeft=100, inTop=500, inWidth=600, inHeight=600, inBus=0, indbRange=96, inLinLogMode = 1;
		var b,c,d,e,f,g, t, u;

		{
			if (dataEvent.window.isNil, {
				dataEvent.busNo = inBus;
				dataEvent.dbRange = indbRange;
				dataEvent.linLogMode = inLinLogMode;



				winColour = winColour ;
				dataEvent.window = Window("Frequency Analysis", Rect(inLeft, inTop, inWidth, inHeight));
				dataEvent.window.view.decorator = FlowLayout(dataEvent.window.view.bounds);
				dataEvent.window.view.background = winColour;
				dataEvent.window.alwaysOnTop = dataEvent.keepOnTop;
				dataEvent.window.front;
				dataEvent.window.onClose = {dataEvent.window = nil; dataEvent.freqScopeView.kill;};

				dataEvent.freqScopeView = FreqScopeView(dataEvent.window, Rect(0, 0, inWidth, inHeight-24));
				dataEvent.freqScopeView.freqMode_(dataEvent.linLogMode);
				dataEvent.freqScopeView.inBus_(dataEvent.busNo);
				dataEvent.freqScopeView.dbRange_(dataEvent.dbRange);
				dataEvent.freqScopeView.active_(true);

				}, {
					dataEvent.window.front;
			});
		}.defer;
	}


	*close {		//	close window
		// OLD if (w.isClosed.not, {w.close;});
		// NEW:
		if (dataEvent.window.notNil,{
			if (dataEvent.window.isClosed.not, {{dataEvent.window.close;}.defer;});
		});
	}


}
