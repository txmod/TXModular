// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
		
TXMultiSliderNo {	// MultiSlider & MultiNumber module with label
	var <>labelView, <>scrollView, <>scrollView2, <>multiSliderView, <>multiNumberView,
		<>controlSpec, <>action, <value;
	
	*new { arg window, dimensions, label, controlSpec, action, initVal, 
			initAction=false, labelWidth=80, scrollViewWidth;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal, 
			initAction, labelWidth, scrollViewWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal, 
			initAction, labelWidth, scrollViewWidth;
		var holdMSVWidth, scrollBox, holdNumberWidth;
		StaticText(window, labelWidth @ dimensions.y);
//		labelView.string = label;
//		labelView.align = \right;
		
		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? Array.fill(8, controlSpec.default);
		action = argAction;
		holdMSVWidth = dimensions.x - labelWidth;
		
		if (scrollViewWidth.notNil, {
			scrollView = ScrollView(window, Rect(0, 0, scrollViewWidth, dimensions.y))
				.hasBorder_(false);
			if (GUI.current.asSymbol == \cocoa, {
				scrollView.autoScrolls_(false);
			});
			scrollView.hasHorizontalScroller = false;
			scrollView.hasVerticalScroller = false;
			scrollBox = CompositeView(scrollView, Rect(0, 0, (initVal.size * 24), dimensions.y));
			scrollBox.decorator = FlowLayout(scrollBox.bounds);
			scrollBox.decorator.margin.x = 0;
			scrollBox.decorator.margin.y = 0;
			scrollBox.decorator.reset;
			holdMSVWidth = (initVal.size+1) * 24;
		});
		multiSliderView = MultiSliderView(scrollBox?window, holdMSVWidth @ (dimensions.y-2));
		multiSliderView.action = {
			value = controlSpec.map(multiSliderView.value);
			multiNumberView.value = value;
			action.value(this);
		};

		// decorator next line & shift 
			if (window.class == Window, {
				window.view.decorator.nextLine;
			}, {
				window.decorator.nextLine;
			});

		holdNumberWidth = ((dimensions.x-labelWidth) / initVal.size ) - 4;
		multiNumberView = TXMultiNumber(window, (dimensions.x-labelWidth) @ 20, label, controlSpec, nil, 
			initVal, numberWidth: holdNumberWidth, scrollViewWidth: scrollViewWidth);
		multiNumberView.action = {
			value = controlSpec.constrain(multiNumberView.value);
			multiSliderView.value = controlSpec.unmap(value);
			action.value(this); 
		};
		scrollView2 = multiNumberView.scrollView;
		labelView = multiNumberView.labelView;

		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			multiNumberView.value = value;
			multiSliderView.value = controlSpec.unmap(value);
		};
	}
	value_ { arg value; multiNumberView.valueAction_(value) }
	valueNoAction_  { arg value; 
		multiNumberView.value = value = controlSpec.constrain(value);
		multiSliderView.value = controlSpec.unmap(value);
	}
	set { arg label, spec, argAction, initVal, initAction=false;
		labelView.string = label;
		controlSpec = spec.asSpec;
		action = argAction;
		initVal = initVal ? Array.fill(8, controlSpec.default);
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			multiNumberView.value = value;
			multiSliderView.value = controlSpec.unmap(value);
		};
	}
}
