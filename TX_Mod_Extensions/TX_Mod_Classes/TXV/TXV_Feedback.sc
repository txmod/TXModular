// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Feedback : TXV_Module {
	classvar <defaultName = "TXV Feedback";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size"]

	// add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXFeedback_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXFeedback_1/drawActive", [
			["bool_int", "/TXFeedback_1/drawActive/fixedValue", 1],
			["bool_int", "/TXFeedback_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/drawActive/useExtMod", 0],
			["float", "/TXFeedback_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXFeedback_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size"]],
		["int", "/TXFeedback_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXFeedback_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXFeedback_1/drawLayersRule", [
			["int", "/TXFeedback_1/drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXFeedback_1/drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/drawLayersRule/useExtMod", 0],
			["float", "/TXFeedback_1/drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXFeedback_1/drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXFeedback_1/drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupBool", "/TXFeedback_1/renderContinuosly", [
			["bool_int", "/TXFeedback_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXFeedback_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/renderContinuosly/useExtMod", 0],
			["float", "/TXFeedback_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXFeedback_1/renderNow", [
			["bool_int", "/TXFeedback_1/renderNow/fixedValue", 0],
			["bool_int", "/TXFeedback_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/renderNow/useExtMod", 0],
			["float", "/TXFeedback_1/renderNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXFeedback_1/freezeFeedback", [
			["bool_int", "/TXFeedback_1/freezeFeedback/fixedValue", 0],
			["bool_int", "/TXFeedback_1/freezeFeedback/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/freezeFeedback/useExtMod", 0],
			["float", "/TXFeedback_1/freezeFeedback/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXFeedback_1/clearNow", [
			["bool_int", "/TXFeedback_1/clearNow/fixedValue", 0],
			["bool_int", "/TXFeedback_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/clearNow/useExtMod", 0],
			["float", "/TXFeedback_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/clearColorHue", [
			["float", "/TXFeedback_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXFeedback_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/clearColorHue/useExtMod", 0],
			["float", "/TXFeedback_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/clearColorSaturation", [
			["float", "/TXFeedback_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXFeedback_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXFeedback_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/clearColorBrightness", [
			["float", "/TXFeedback_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXFeedback_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/clearColorAlpha", [
			["float", "/TXFeedback_1/clearColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXFeedback_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXFeedback_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXFeedback_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackAlpha", [
			["float", "/TXFeedback_1/feedbackAlpha/fixedValue", 0.5, 0, 1],
			["float", "/TXFeedback_1/feedbackAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackAlpha/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackAlpha/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackAlpha/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackBrightness", [
			["float", "/TXFeedback_1/feedbackBrightness/fixedValue", 1, 0, 1],
			["float", "/TXFeedback_1/feedbackBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackBrightness/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackBrightness/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackBrightness/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackShiftX", [
			["float", "/TXFeedback_1/feedbackShiftX/fixedValue", 0.0, -100.0, 100.0],
			["float", "/TXFeedback_1/feedbackShiftX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackShiftX/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackShiftX/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackShiftX/softMin", -100.0, -100.0, 100.0],
			["float", "/TXFeedback_1/feedbackShiftX/softMax", 100.0, -100.0, 100.0],
		]],
		[ "modParameterGroupBool", "/TXFeedback_1/quantiseFeedbackShiftX", [
			["bool_int", "/TXFeedback_1/quantiseFeedbackShiftX/fixedValue", 0],
			["bool_int", "/TXFeedback_1/quantiseFeedbackShiftX/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/quantiseFeedbackShiftX/useExtMod", 0],
			["float", "/TXFeedback_1/quantiseFeedbackShiftX/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackShiftY", [
			["float", "/TXFeedback_1/feedbackShiftY/fixedValue", 0.0, -100.0, 100.0],
			["float", "/TXFeedback_1/feedbackShiftY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackShiftY/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackShiftY/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackShiftY/softMin", -100.0, -100.0, 100.0],
			["float", "/TXFeedback_1/feedbackShiftY/softMax", 100.0, -100.0, 100.0],
		]],
		[ "modParameterGroupBool", "/TXFeedback_1/quantiseFeedbackShiftY", [
			["bool_int", "/TXFeedback_1/quantiseFeedbackShiftY/fixedValue", 0],
			["bool_int", "/TXFeedback_1/quantiseFeedbackShiftY/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/quantiseFeedbackShiftY/useExtMod", 0],
			["float", "/TXFeedback_1/quantiseFeedbackShiftY/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackAnchorX", [
			["float", "/TXFeedback_1/feedbackAnchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXFeedback_1/feedbackAnchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackAnchorX/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackAnchorX/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackAnchorX/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackAnchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXFeedback_1/quantiseFeedbackAnchorX", [
			["bool_int", "/TXFeedback_1/quantiseFeedbackAnchorX/fixedValue", 0],
			["bool_int", "/TXFeedback_1/quantiseFeedbackAnchorX/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/quantiseFeedbackAnchorX/useExtMod", 0],
			["float", "/TXFeedback_1/quantiseFeedbackAnchorX/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackAnchorY", [
			["float", "/TXFeedback_1/feedbackAnchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXFeedback_1/feedbackAnchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackAnchorY/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackAnchorY/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackAnchorY/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackAnchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXFeedback_1/quantiseFeedbackAnchorY", [
			["bool_int", "/TXFeedback_1/quantiseFeedbackAnchorY/fixedValue", 0],
			["bool_int", "/TXFeedback_1/quantiseFeedbackAnchorY/fixedModMix", 0],
			["bool_int", "/TXFeedback_1/quantiseFeedbackAnchorY/useExtMod", 0],
			["float", "/TXFeedback_1/quantiseFeedbackAnchorY/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackRotate", [
			["float", "/TXFeedback_1/feedbackRotate/fixedValue", 0, -360, 360],
			["float", "/TXFeedback_1/feedbackRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackRotate/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackRotate/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackRotate/softMin", -360, -360, 360],
			["float", "/TXFeedback_1/feedbackRotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackRotateMultiply", [
			["float", "/TXFeedback_1/feedbackRotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXFeedback_1/feedbackRotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackRotateMultiply/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackRotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackRotateMultiply/softMin", 0, 0, 10],
			["float", "/TXFeedback_1/feedbackRotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackScaleX", [
			["float", "/TXFeedback_1/feedbackScaleX/fixedValue", 1, 0, 3],
			["float", "/TXFeedback_1/feedbackScaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackScaleX/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackScaleX/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackScaleX/softMin", 0, -10, 10],
			["float", "/TXFeedback_1/feedbackScaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/feedbackScaleY", [
			["float", "/TXFeedback_1/feedbackScaleY/fixedValue", 1, 0, 3],
			["float", "/TXFeedback_1/feedbackScaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/feedbackScaleY/useExtMod", 0],
			["float", "/TXFeedback_1/feedbackScaleY/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/feedbackScaleY/softMin", 0, -10, 10],
			["float", "/TXFeedback_1/feedbackScaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXFeedback_1/useFeedbackScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXFeedback_1/drawAlpha", [
			["float", "/TXFeedback_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXFeedback_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/drawAlpha/useExtMod", 0],
			["float", "/TXFeedback_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/drawPosX", [
			["float", "/TXFeedback_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXFeedback_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/drawPosX/useExtMod", 0],
			["float", "/TXFeedback_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/drawPosY", [
			["float", "/TXFeedback_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXFeedback_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/drawPosY/useExtMod", 0],
			["float", "/TXFeedback_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/drawWidth", [
			["float", "/TXFeedback_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXFeedback_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/drawWidth/useExtMod", 0],
			["float", "/TXFeedback_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/drawHeight", [
			["float", "/TXFeedback_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXFeedback_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/drawHeight/useExtMod", 0],
			["float", "/TXFeedback_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/anchorX", [
			["float", "/TXFeedback_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXFeedback_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/anchorX/useExtMod", 0],
			["float", "/TXFeedback_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/anchorY", [
			["float", "/TXFeedback_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXFeedback_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/anchorY/useExtMod", 0],
			["float", "/TXFeedback_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXFeedback_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/rotate", [
			["float", "/TXFeedback_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXFeedback_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/rotate/useExtMod", 0],
			["float", "/TXFeedback_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/rotate/softMin", -360, -360, 360],
			["float", "/TXFeedback_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/rotateMultiply", [
			["float", "/TXFeedback_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXFeedback_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/rotateMultiply/useExtMod", 0],
			["float", "/TXFeedback_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXFeedback_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/scaleX", [
			["float", "/TXFeedback_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXFeedback_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/scaleX/useExtMod", 0],
			["float", "/TXFeedback_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXFeedback_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXFeedback_1/scaleY", [
			["float", "/TXFeedback_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXFeedback_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFeedback_1/scaleY/useExtMod", 0],
			["float", "/TXFeedback_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXFeedback_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXFeedback_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXFeedback_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXFeedback_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Shift & Scale", {arg parameterName;
				parameterName.containsi("feedbackshift")
				|| parameterName.containsi("feedbackscale")
				;}],
			["Anchor & Rotate", {arg parameterName;
				parameterName.containsi("feedbackrotate")
				|| parameterName.containsi("feedbackanchor")
				;}],
			["Clear & Render$Clear", {arg parameterName;
				parameterName.containsi("clear")
				;}],
			["Clear & Render$Render", {arg parameterName;
				parameterName.containsi("render")
				&& not(parameterName.containsi("clear"))
				|| parameterName.containsi("drawLayersRule")
				;}],
			["Draw$Active, Size, Alpha", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth", "useSamplePosForDrawPos",
					"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
					"drawAlpha", "drawWidth", "drawHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "anchorX", "anchorY",
					"rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Freeze, Alpha, Brightness";
	}


}


