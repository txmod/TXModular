// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

ModMatrixRowScale {
	var <>popupMenuView, <>popupMenuView2, <>popupMenuView3, <>numberView, <>sliderView;
	var sourceIndex=0, destIndex=0, numValue=0, scaleIndex=0, controlSpec;

// e.g.	holdView = ModMatrixRow(w, viewWidth @ 20, arrMMSourceNames, arrMMDestNames, getSourceFunc,
//	 setSourceFunc, getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc, arrMMScaleNames,
//	getScaleFunc, setScaleFunc);

	*new { arg window, dimensions, arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc,
			getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc, arrMMScaleNames, getScaleFunc, setScaleFunc, sliderWidth;
		^super.new.init(window, dimensions, arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc,
			getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc, arrMMScaleNames, getScaleFunc, setScaleFunc, sliderWidth);
	}
	init { arg window, dimensions, arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc,
			getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc, arrMMScaleNames, getScaleFunc, setScaleFunc, sliderWidth = 115;

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
		sliderView = Slider(window, sliderWidth @ dimensions.y);
		sliderView.thumbSize_(8).knobColor_(Color.white);
		sliderView.action = {
			numValue = controlSpec.map(sliderView.value);
			numberView.value = numValue.round(1);
			setMMValueFunc.value(numValue);
		};

		// number
		numberView = TXScrollNumBox(window, 35 @ dimensions.y);
		numberView.action = {
			numberView.value = numValue = controlSpec.constrain(numberView.value);
			sliderView.value =  controlSpec.unmap(numberView.value);
			setMMValueFunc.value(numberView.value);
		};

		// scale popup
		popupMenuView3 = PopUpMenu(window, 100 @ dimensions.y);
		popupMenuView3.items = arrMMScaleNames;
		popupMenuView3.action = {
			scaleIndex = popupMenuView3.value;
			setScaleFunc.value(popupMenuView3.value);
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
		popupMenuView3.value = scaleIndex = arrVals.at(3);
		this.adjustVisibility;
	}
	valueAll{
		^[sourceIndex, destIndex, numValue, scaleIndex];
	}
}



