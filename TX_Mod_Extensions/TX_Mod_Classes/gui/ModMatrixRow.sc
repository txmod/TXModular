// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

ModMatrixRow {
	var <>popupMenuView, <>popupMenuView2, <>numberView, <>sliderView;
	var sourceIndex=0, destIndex=0, numValue=0, controlSpec;

// e.g.	holdView = ModMatrixRow(w, viewWidth @ 20, arrMMSourceNames, arrMMDestNames, getSourceFunc,
//	 setSourceFunc, getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc);

	*new { arg window, dimensions, arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc,
			getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc;
		^super.new.init(window, dimensions, arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc,
			getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc);
	}
	init { arg window, dimensions, arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc,
			getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc;

		controlSpec = [-100,100].asSpec;

		// source popup
		popupMenuView = PopUpMenu(window, 100 @ dimensions.y);
		popupMenuView.items = arrMMSourceNames;
		popupMenuView.action = {
			sourceIndex = popupMenuView.value;
			setSourceFunc.value(popupMenuView.value);
			this.adjustVisibility;
		};

		// dest popup
		popupMenuView2 = PopUpMenu(window, 100 @ dimensions.y);
		popupMenuView2.items = arrMMDestNames;
		popupMenuView2.action = {
			destIndex = popupMenuView2.value;
			setDestFunc.value(popupMenuView2.value);
			this.adjustVisibility;
		};

		// slider
		sliderView = Slider(window, (dimensions.x - 258) @ dimensions.y);
		sliderView.thumbSize_(8).knobColor_(Color.white);
		sliderView.action = {
			numValue = controlSpec.map(sliderView.value);
			numberView.value = numValue;
			setMMValueFunc.value(numValue);
		};

		// number
		numberView = TXScrollNumBox(window, 50 @ dimensions.y);
		numberView.action = {
			numberView.value = numValue = controlSpec.constrain(numberView.value);
			sliderView.value =  controlSpec.unmap(numberView.value);
			setMMValueFunc.value(numberView.value);
		};

		this.adjustVisibility;
	}
	adjustVisibility {
		if ( (sourceIndex > 0) and: (destIndex > 0), {
			numberView.visible_(true);
			sliderView.visible_(true);
		}, {
			numberView.visible_(false);
			sliderView.visible_(false);
		});
	}
	valueAll_ { arg arrVals;
		popupMenuView.value = sourceIndex = arrVals.at(0);
		popupMenuView2.value = destIndex = arrVals.at(1);
		numberView.value = numValue = controlSpec.constrain(arrVals.at(2));
		sliderView.value = controlSpec.unmap(numValue);
		this.adjustVisibility;
	}
	valueAll{
		^[sourceIndex, destIndex, numValue];
	}
}



