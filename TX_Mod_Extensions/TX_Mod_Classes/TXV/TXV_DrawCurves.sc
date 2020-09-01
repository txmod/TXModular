// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawCurves : TXV_Module {
	classvar <defaultName = "TXV Curves";
	classvar <>arrInstances;
	var <>arrSlotData;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."curveType/fixedValue":
	// , ["Automatic curve", "Straight line", "Bezier curve using control point 1", "Bezier curve using control point 1, smoothed", "Bezier curve using control points 1 & 2"]

	// add text array to ..."windingMode/fixedValue":
	// , ["Odd - allows holes (default)", "Non-Zero - outline, no holes,  autoClose", ]

	// add text array to ... "transformOrder/fixedValue"
	// , ["Shift Rotate Scale", "Shift Scale Rotate", "Rotate Scale Shift", "Rotate Shift Scale", "Scale Shift Rotate", "Scale Rotate Shift"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawCurves_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawCurves_1/drawActive", [
			["bool_int", "/TXDrawCurves_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawCurves_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawCurves_1/drawActive/useExtMod", 0],
			["float", "/TXDrawCurves_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawCurves_1/maxWidthHeightRule", 0, 0, 5,
			["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawCurves_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawCurves_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXDrawCurves_1/curveType", [
			["int", "/TXDrawCurves_1/curveType/fixedValue", 4, 0, 4,
				["Automatic curve", "Straight line", "Bezier curve using control point 1", "Bezier curve using control point 1, smoothed", "Bezier curve using control points 1 & 2"]],
			["float", "/TXDrawCurves_1/curveType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/curveType/useExtMod", 0],
			["float", "/TXDrawCurves_1/curveType/extModValue", 0, 0, 1],
			["int", "/TXDrawCurves_1/curveType/softMin", 0, 0, 4],
			["int", "/TXDrawCurves_1/curveType/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawCurves_1/numPoints", [
			["int", "/TXDrawCurves_1/numPoints/fixedValue", 8, 2, 8],
			["float", "/TXDrawCurves_1/numPoints/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/numPoints/useExtMod", 0],
			["float", "/TXDrawCurves_1/numPoints/extModValue", 0, 0, 1],
			["int", "/TXDrawCurves_1/numPoints/softMin", 2, 2, 8],
			["int", "/TXDrawCurves_1/numPoints/softMax", 8, 2, 8],
		]],
		[ "modParameterGroupInt", "/TXDrawCurves_1/numCurves", [
			["int", "/TXDrawCurves_1/numCurves/fixedValue", 12, 1, 100],
			["float", "/TXDrawCurves_1/numCurves/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/numCurves/useExtMod", 0],
			["float", "/TXDrawCurves_1/numCurves/extModValue", 0, 0, 1],
			["int", "/TXDrawCurves_1/numCurves/softMin", 0, 0, 1000],
			["int", "/TXDrawCurves_1/numCurves/softMax", 100, 0, 1000],
		]],
		[ "modParameterGroupBool", "/TXDrawCurves_1/ifSingleIgnoreEndValues", [
			["bool_int", "/TXDrawCurves_1/ifSingleIgnoreEndValues/fixedValue", 0],
			["bool_int", "/TXDrawCurves_1/ifSingleIgnoreEndValues/fixedModMix", 0],
			["bool_int", "/TXDrawCurves_1/ifSingleIgnoreEndValues/useExtMod", 0],
			["float", "/TXDrawCurves_1/ifSingleIgnoreEndValues/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawCurves_1/lineWidth", [
			["int", "/TXDrawCurves_1/lineWidth/fixedValue", 4, 0, 10],
			["float", "/TXDrawCurves_1/lineWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineWidth/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineWidth/extModValue", 0, 0, 1],
			["int", "/TXDrawCurves_1/lineWidth/softMin", 0, 0, 10],
			["int", "/TXDrawCurves_1/lineWidth/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawCurves_1/autoClose", [
			["bool_int", "/TXDrawCurves_1/autoClose/fixedValue", 0],
			["bool_int", "/TXDrawCurves_1/autoClose/fixedModMix", 0],
			["bool_int", "/TXDrawCurves_1/autoClose/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoClose/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawCurves_1/fillShape", [
			["bool_int", "/TXDrawCurves_1/fillShape/fixedValue", 0],
			["bool_int", "/TXDrawCurves_1/fillShape/fixedModMix", 0],
			["bool_int", "/TXDrawCurves_1/fillShape/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillShape/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawCurves_1/windingMode", [
			["int", "/TXDrawCurves_1/windingMode/fixedValue", 0, 0, 1, ["Odd - allows holes (default)", "Non-Zero - outline, no holes,  autoClose", ]],
			["float", "/TXDrawCurves_1/windingMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/windingMode/useExtMod", 0],
			["float", "/TXDrawCurves_1/windingMode/extModValue", 0, 0, 1],
			["int", "/TXDrawCurves_1/windingMode/softMin", 0, 0, 1],
			["int", "/TXDrawCurves_1/windingMode/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawCurves_1/curveResolution", [
			["int", "/TXDrawCurves_1/curveResolution/fixedValue", 64, 2, 128],
			["float", "/TXDrawCurves_1/curveResolution/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/curveResolution/useExtMod", 0],
			["float", "/TXDrawCurves_1/curveResolution/extModValue", 0, 0, 1],
			["int", "/TXDrawCurves_1/curveResolution/softMin", 2, 2, 1024],
			["int", "/TXDrawCurves_1/curveResolution/softMax", 128, 2, 1024],
		]],
		[ "modParameterGroupBool", "/TXDrawCurves_1/reverseCurveOrder", [
			["bool_int", "/TXDrawCurves_1/reverseCurveOrder/fixedValue", 0],
			["bool_int", "/TXDrawCurves_1/reverseCurveOrder/fixedModMix", 0],
			["bool_int", "/TXDrawCurves_1/reverseCurveOrder/useExtMod", 0],
			["float", "/TXDrawCurves_1/reverseCurveOrder/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawCurves_1/drawControlPoints", [
			["bool_int", "/TXDrawCurves_1/drawControlPoints/fixedValue", 0],
			["bool_int", "/TXDrawCurves_1/drawControlPoints/fixedModMix", 0],
			["bool_int", "/TXDrawCurves_1/drawControlPoints/useExtMod", 0],
			["float", "/TXDrawCurves_1/drawControlPoints/extModValue", 0, 0, 1],
		]],
		["float256", "/TXDrawCurves_1/positionSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawCurves_1/positionSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/positionSpreadCurveMorph", [
			["float", "/TXDrawCurves_1/positionSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/positionSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/positionSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawCurves_1/positionSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/positionSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/positionSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/positionSpreadCurvePhase", [
			["float", "/TXDrawCurves_1/positionSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/positionSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/positionSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawCurves_1/positionSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/positionSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/positionSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/swapLineColorWithFillColor", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorHue", [
			["float", "/TXDrawCurves_1/lineColorHue/fixedValue", 0.4, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorHue/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorHue/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHue/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorHueRotate", [
			["float", "/TXDrawCurves_1/lineColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorHueRotate/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorSaturation", [
			["float", "/TXDrawCurves_1/lineColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawCurves_1/lineColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorSaturation/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorBrightness", [
			["float", "/TXDrawCurves_1/lineColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/lineColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorBrightness/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorAlpha", [
			["float", "/TXDrawCurves_1/lineColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawCurves_1/lineColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorAlpha/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorAlpha/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorHueEnd", [
			["float", "/TXDrawCurves_1/lineColorHueEnd/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorHueEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorHueEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorHueRotateEnd", [
			["float", "/TXDrawCurves_1/lineColorHueRotateEnd/fixedValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueRotateEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorHueRotateEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorHueRotateEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueRotateEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorHueRotateEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorSaturationEnd", [
			["float", "/TXDrawCurves_1/lineColorSaturationEnd/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawCurves_1/lineColorSaturationEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorSaturationEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorSaturationEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorSaturationEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorSaturationEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorBrightnessEnd", [
			["float", "/TXDrawCurves_1/lineColorBrightnessEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/lineColorBrightnessEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorBrightnessEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorBrightnessEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorBrightnessEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorBrightnessEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/lineColorAlphaEnd", [
			["float", "/TXDrawCurves_1/lineColorAlphaEnd/fixedValue", 1, 0, 1],
			["float", "/TXDrawCurves_1/lineColorAlphaEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/lineColorAlphaEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/lineColorAlphaEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorAlphaEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/lineColorAlphaEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/useLineColorHueForEndColor", 0],
		["bool_int", "/TXDrawCurves_1/useLineColorSaturationForEndColor", 0],
		["bool_int", "/TXDrawCurves_1/useLineColorBrightnessForEndColor", 0],
		["bool_int", "/TXDrawCurves_1/useLineColorAlphaForEndColor", 0],
		["bool_int", "/TXDrawCurves_1/lineColorHSBAsRGB", 0],
		["bool_int", "/TXDrawCurves_1/useLineColorForFillColor", 1],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorHue", [
			["float", "/TXDrawCurves_1/fillColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorHue/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorHue/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHue/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorHueRotate", [
			["float", "/TXDrawCurves_1/fillColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorHueRotate/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorSaturation", [
			["float", "/TXDrawCurves_1/fillColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawCurves_1/fillColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorSaturation/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorBrightness", [
			["float", "/TXDrawCurves_1/fillColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/fillColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorBrightness/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorAlpha", [
			["float", "/TXDrawCurves_1/fillColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawCurves_1/fillColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorAlpha/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorAlpha/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorHueEnd", [
			["float", "/TXDrawCurves_1/fillColorHueEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorHueEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorHueEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorHueRotateEnd", [
			["float", "/TXDrawCurves_1/fillColorHueRotateEnd/fixedValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueRotateEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorHueRotateEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorHueRotateEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueRotateEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorHueRotateEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorSaturationEnd", [
			["float", "/TXDrawCurves_1/fillColorSaturationEnd/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawCurves_1/fillColorSaturationEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorSaturationEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorSaturationEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorSaturationEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorSaturationEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorBrightnessEnd", [
			["float", "/TXDrawCurves_1/fillColorBrightnessEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/fillColorBrightnessEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorBrightnessEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorBrightnessEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorBrightnessEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorBrightnessEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/fillColorAlphaEnd", [
			["float", "/TXDrawCurves_1/fillColorAlphaEnd/fixedValue", 1, 0, 1],
			["float", "/TXDrawCurves_1/fillColorAlphaEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/fillColorAlphaEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/fillColorAlphaEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorAlphaEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/fillColorAlphaEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/useFillColorHueForEndColor", 0],
		["bool_int", "/TXDrawCurves_1/useFillColorSaturationForEndColor", 0],
		["bool_int", "/TXDrawCurves_1/useFillColorBrightnessForEndColor", 0],
		["bool_int", "/TXDrawCurves_1/useFillColorAlphaForEndColor", 0],
		["bool_int", "/TXDrawCurves_1/fillColorHSBAsRGB", 0],
		["float256", "/TXDrawCurves_1/colorSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawCurves_1/colorSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/colorSpreadCurveMorph", [
			["float", "/TXDrawCurves_1/colorSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/colorSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/colorSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawCurves_1/colorSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/colorSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/colorSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/colorSpreadCurvePhase", [
			["float", "/TXDrawCurves_1/colorSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/colorSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/colorSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawCurves_1/colorSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/colorSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/colorSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/usePoint1ForAutoCurveStart", 0],
		["bool_int", "/TXDrawCurves_1/useLastPointForAutoCurveEnd", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point1_drawX", [
			["float", "/TXDrawCurves_1/point1_drawX/fixedValue", 0.267, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point1_drawX/useExtMod", 0],
			["float", "/TXDrawCurves_1/point1_drawX/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawX/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point1_drawY", [
			["float", "/TXDrawCurves_1/point1_drawY/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point1_drawY/useExtMod", 0],
			["float", "/TXDrawCurves_1/point1_drawY/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawY/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point1_drawXEnd", [
			["float", "/TXDrawCurves_1/point1_drawXEnd/fixedValue", 0.267, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point1_drawXEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point1_drawXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawXEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawXEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point1_drawYEnd", [
			["float", "/TXDrawCurves_1/point1_drawYEnd/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point1_drawYEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point1_drawYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawYEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point1_drawYEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point1_drawXEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point1_drawYEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_control1X", [
			["float", "/TXDrawCurves_1/point2_control1X/fixedValue", 0.133, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_control1Y", [
			["float", "/TXDrawCurves_1/point2_control1Y/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_control1XEnd", [
			["float", "/TXDrawCurves_1/point2_control1XEnd/fixedValue", 0.133, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_control1YEnd", [
			["float", "/TXDrawCurves_1/point2_control1YEnd/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point2_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point2_control1YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_control2X", [
			["float", "/TXDrawCurves_1/point2_control2X/fixedValue", 0.067, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_control2X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_control2X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_control2Y", [
			["float", "/TXDrawCurves_1/point2_control2Y/fixedValue", 0.633, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_control2Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_control2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_control2XEnd", [
			["float", "/TXDrawCurves_1/point2_control2XEnd/fixedValue", 0.133, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_control2XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_control2XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_control2YEnd", [
			["float", "/TXDrawCurves_1/point2_control2YEnd/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_control2YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_control2YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_control2YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point2_control2XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point2_control2YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_drawX", [
			["float", "/TXDrawCurves_1/point2_drawX/fixedValue", 0.267, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_drawX/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_drawX/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawX/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_drawY", [
			["float", "/TXDrawCurves_1/point2_drawY/fixedValue", 0.567, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_drawY/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_drawY/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawY/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_drawXEnd", [
			["float", "/TXDrawCurves_1/point2_drawXEnd/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_drawXEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_drawXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawXEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawXEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point2_drawYEnd", [
			["float", "/TXDrawCurves_1/point2_drawYEnd/fixedValue", 0.767, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point2_drawYEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point2_drawYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawYEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point2_drawYEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point2_drawXEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point2_drawYEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_control1X", [
			["float", "/TXDrawCurves_1/point3_control1X/fixedValue", 0.433, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_control1Y", [
			["float", "/TXDrawCurves_1/point3_control1Y/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_control1XEnd", [
			["float", "/TXDrawCurves_1/point3_control1XEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_control1YEnd", [
			["float", "/TXDrawCurves_1/point3_control1YEnd/fixedValue", 0.7, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point3_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point3_control1YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_control2X", [
			["float", "/TXDrawCurves_1/point3_control2X/fixedValue", 0.433, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_control2X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_control2X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_control2Y", [
			["float", "/TXDrawCurves_1/point3_control2Y/fixedValue", 0.367, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_control2Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_control2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_control2XEnd", [
			["float", "/TXDrawCurves_1/point3_control2XEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_control2XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_control2XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_control2YEnd", [
			["float", "/TXDrawCurves_1/point3_control2YEnd/fixedValue", 0.267, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_control2YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_control2YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_control2YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point3_control2XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point3_control2YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_drawX", [
			["float", "/TXDrawCurves_1/point3_drawX/fixedValue", 0.267, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_drawX/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_drawX/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawX/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_drawY", [
			["float", "/TXDrawCurves_1/point3_drawY/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_drawY/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_drawY/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawY/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_drawXEnd", [
			["float", "/TXDrawCurves_1/point3_drawXEnd/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_drawXEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_drawXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawXEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawXEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point3_drawYEnd", [
			["float", "/TXDrawCurves_1/point3_drawYEnd/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point3_drawYEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point3_drawYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawYEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point3_drawYEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point3_drawXEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point3_drawYEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_control1X", [
			["float", "/TXDrawCurves_1/point4_control1X/fixedValue", 0.133, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_control1Y", [
			["float", "/TXDrawCurves_1/point4_control1Y/fixedValue", 0.267, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_control1XEnd", [
			["float", "/TXDrawCurves_1/point4_control1XEnd/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_control1YEnd", [
			["float", "/TXDrawCurves_1/point4_control1YEnd/fixedValue", 0.133, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point4_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point4_control1YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_control2X", [
			["float", "/TXDrawCurves_1/point4_control2X/fixedValue", 0.167, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_control2X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_control2X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_control2Y", [
			["float", "/TXDrawCurves_1/point4_control2Y/fixedValue", 0.167, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_control2Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_control2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_control2XEnd", [
			["float", "/TXDrawCurves_1/point4_control2XEnd/fixedValue", 0.067, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_control2XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_control2XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_control2YEnd", [
			["float", "/TXDrawCurves_1/point4_control2YEnd/fixedValue", 0.133, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_control2YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_control2YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_control2YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point4_control2XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point4_control2YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_drawX", [
			["float", "/TXDrawCurves_1/point4_drawX/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_drawX/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_drawX/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawX/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_drawY", [
			["float", "/TXDrawCurves_1/point4_drawY/fixedValue", 0.1, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_drawY/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_drawY/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawY/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_drawXEnd", [
			["float", "/TXDrawCurves_1/point4_drawXEnd/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_drawXEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_drawXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawXEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawXEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point4_drawYEnd", [
			["float", "/TXDrawCurves_1/point4_drawYEnd/fixedValue", 0.067, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point4_drawYEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point4_drawYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawYEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point4_drawYEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point4_drawXEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point4_drawYEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_control1X", [
			["float", "/TXDrawCurves_1/point5_control1X/fixedValue", 0.4, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_control1Y", [
			["float", "/TXDrawCurves_1/point5_control1Y/fixedValue", 0.033, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_control1XEnd", [
			["float", "/TXDrawCurves_1/point5_control1XEnd/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_control1YEnd", [
			["float", "/TXDrawCurves_1/point5_control1YEnd/fixedValue", 0.0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point5_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point5_control1YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_control2X", [
			["float", "/TXDrawCurves_1/point5_control2X/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_control2X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_control2X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_control2Y", [
			["float", "/TXDrawCurves_1/point5_control2Y/fixedValue", 0.033, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_control2Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_control2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_control2XEnd", [
			["float", "/TXDrawCurves_1/point5_control2XEnd/fixedValue", 0.633, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_control2XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_control2XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_control2YEnd", [
			["float", "/TXDrawCurves_1/point5_control2YEnd/fixedValue", 0.0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_control2YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_control2YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_control2YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point5_control2XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point5_control2YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_drawX", [
			["float", "/TXDrawCurves_1/point5_drawX/fixedValue", 0.767, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_drawX/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_drawX/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawX/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_drawY", [
			["float", "/TXDrawCurves_1/point5_drawY/fixedValue", 0.067, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_drawY/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_drawY/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawY/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_drawXEnd", [
			["float", "/TXDrawCurves_1/point5_drawXEnd/fixedValue", 0.833, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_drawXEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_drawXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawXEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawXEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point5_drawYEnd", [
			["float", "/TXDrawCurves_1/point5_drawYEnd/fixedValue", 0.1, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point5_drawYEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point5_drawYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawYEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point5_drawYEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point5_drawXEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point5_drawYEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_control1X", [
			["float", "/TXDrawCurves_1/point6_control1X/fixedValue", 0.967, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_control1Y", [
			["float", "/TXDrawCurves_1/point6_control1Y/fixedValue", 0.1, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_control1XEnd", [
			["float", "/TXDrawCurves_1/point6_control1XEnd/fixedValue", 1.0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_control1YEnd", [
			["float", "/TXDrawCurves_1/point6_control1YEnd/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point6_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point6_control1YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_control2X", [
			["float", "/TXDrawCurves_1/point6_control2X/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_control2X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_control2X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_control2Y", [
			["float", "/TXDrawCurves_1/point6_control2Y/fixedValue", 0.267, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_control2Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_control2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_control2XEnd", [
			["float", "/TXDrawCurves_1/point6_control2XEnd/fixedValue", 0.767, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_control2XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_control2XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_control2YEnd", [
			["float", "/TXDrawCurves_1/point6_control2YEnd/fixedValue", 0.267, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_control2YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_control2YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_control2YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point6_control2XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point6_control2YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_drawX", [
			["float", "/TXDrawCurves_1/point6_drawX/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_drawX/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_drawX/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawX/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_drawY", [
			["float", "/TXDrawCurves_1/point6_drawY/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_drawY/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_drawY/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawY/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_drawXEnd", [
			["float", "/TXDrawCurves_1/point6_drawXEnd/fixedValue", 0.633, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_drawXEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_drawXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawXEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawXEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point6_drawYEnd", [
			["float", "/TXDrawCurves_1/point6_drawYEnd/fixedValue", 0.367, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point6_drawYEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point6_drawYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawYEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point6_drawYEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point6_drawXEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point6_drawYEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_control1X", [
			["float", "/TXDrawCurves_1/point7_control1X/fixedValue", 0.633, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_control1Y", [
			["float", "/TXDrawCurves_1/point7_control1Y/fixedValue", 0.367, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_control1XEnd", [
			["float", "/TXDrawCurves_1/point7_control1XEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_control1YEnd", [
			["float", "/TXDrawCurves_1/point7_control1YEnd/fixedValue", 0.467, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point7_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point7_control1YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_control2X", [
			["float", "/TXDrawCurves_1/point7_control2X/fixedValue", 0.567, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_control2X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_control2X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_control2Y", [
			["float", "/TXDrawCurves_1/point7_control2Y/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_control2Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_control2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_control2XEnd", [
			["float", "/TXDrawCurves_1/point7_control2XEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_control2XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_control2XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_control2YEnd", [
			["float", "/TXDrawCurves_1/point7_control2YEnd/fixedValue", 0.567, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_control2YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_control2YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_control2YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point7_control2XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point7_control2YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_drawX", [
			["float", "/TXDrawCurves_1/point7_drawX/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_drawX/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_drawX/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawX/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_drawY", [
			["float", "/TXDrawCurves_1/point7_drawY/fixedValue", 0.567, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_drawY/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_drawY/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawY/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_drawXEnd", [
			["float", "/TXDrawCurves_1/point7_drawXEnd/fixedValue", 0.767, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_drawXEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_drawXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawXEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawXEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point7_drawYEnd", [
			["float", "/TXDrawCurves_1/point7_drawYEnd/fixedValue", 0.633, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point7_drawYEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point7_drawYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawYEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point7_drawYEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point7_drawXEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point7_drawYEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_control1X", [
			["float", "/TXDrawCurves_1/point8_control1X/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_control1Y", [
			["float", "/TXDrawCurves_1/point8_control1Y/fixedValue", 0.633, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_control1XEnd", [
			["float", "/TXDrawCurves_1/point8_control1XEnd/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_control1YEnd", [
			["float", "/TXDrawCurves_1/point8_control1YEnd/fixedValue", 0.667, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point8_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point8_control1YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_control2X", [
			["float", "/TXDrawCurves_1/point8_control2X/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_control2X/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_control2X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_control2Y", [
			["float", "/TXDrawCurves_1/point8_control2Y/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_control2Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_control2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_control2XEnd", [
			["float", "/TXDrawCurves_1/point8_control2XEnd/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_control2XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_control2XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_control2YEnd", [
			["float", "/TXDrawCurves_1/point8_control2YEnd/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_control2YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_control2YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_control2YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point8_control2XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point8_control2YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_drawX", [
			["float", "/TXDrawCurves_1/point8_drawX/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_drawX/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_drawX/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawX/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_drawY", [
			["float", "/TXDrawCurves_1/point8_drawY/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_drawY/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_drawY/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawY/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_drawXEnd", [
			["float", "/TXDrawCurves_1/point8_drawXEnd/fixedValue", 0.8, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_drawXEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_drawXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawXEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawXEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/point8_drawYEnd", [
			["float", "/TXDrawCurves_1/point8_drawYEnd/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/point8_drawYEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/point8_drawYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawYEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/point8_drawYEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/point8_drawXEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/point8_drawYEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/autoCurveStart_control1X", [
			["float", "/TXDrawCurves_1/autoCurveStart_control1X/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/autoCurveStart_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoCurveStart_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/autoCurveStart_control1Y", [
			["float", "/TXDrawCurves_1/autoCurveStart_control1Y/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/autoCurveStart_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoCurveStart_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/autoCurveStart_control1XEnd", [
			["float", "/TXDrawCurves_1/autoCurveStart_control1XEnd/fixedValue", 0.4, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/autoCurveStart_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoCurveStart_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/autoCurveStart_control1YEnd", [
			["float", "/TXDrawCurves_1/autoCurveStart_control1YEnd/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/autoCurveStart_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoCurveStart_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveStart_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/autoCurveStart_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/autoCurveStart_control1YEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/autoCurveEnd_control1X", [
			["float", "/TXDrawCurves_1/autoCurveEnd_control1X/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/autoCurveEnd_control1X/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1X/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1X/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1X/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/autoCurveEnd_control1Y", [
			["float", "/TXDrawCurves_1/autoCurveEnd_control1Y/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/autoCurveEnd_control1Y/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1Y/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1Y/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/autoCurveEnd_control1XEnd", [
			["float", "/TXDrawCurves_1/autoCurveEnd_control1XEnd/fixedValue", 0.667, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1XEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/autoCurveEnd_control1XEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1XEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1XEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1XEnd/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawCurves_1/autoCurveEnd_control1YEnd", [
			["float", "/TXDrawCurves_1/autoCurveEnd_control1YEnd/fixedValue", 0.933, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1YEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawCurves_1/autoCurveEnd_control1YEnd/useExtMod", 0],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1YEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1YEnd/softMin", 0, 0, 1],
			["float", "/TXDrawCurves_1/autoCurveEnd_control1YEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawCurves_1/autoCurveEnd_control1XEndIgnore", 0],
		["bool_int", "/TXDrawCurves_1/autoCurveEnd_control1YEndIgnore", 0],
		[ "modParameterGroupInt", "/TXDrawTransform_1/transformOrder", [
			["int", "/TXDrawTransform_1/transformOrder/fixedValue", 0, 0, 5, ["Shift Rotate Scale", "Shift Scale Rotate", "Rotate Scale Shift", "Rotate Shift Scale", "Scale Shift Rotate", "Scale Rotate Shift"]],
			["float", "/TXDrawTransform_1/transformOrder/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform_1/transformOrder/useExtMod", 0],
			["float", "/TXDrawTransform_1/transformOrder/extModValue", 0, 0, 1],
			["int", "/TXDrawTransform_1/transformOrder/softMin", 0, 0, 5],
			["int", "/TXDrawTransform_1/transformOrder/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform_1/shiftX", [
			["float", "/TXDrawTransform_1/shiftX/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform_1/shiftX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform_1/shiftX/useExtMod", 0],
			["float", "/TXDrawTransform_1/shiftX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform_1/shiftX/softMin", -1, -10, 10],
			["float", "/TXDrawTransform_1/shiftX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform_1/shiftY", [
			["float", "/TXDrawTransform_1/shiftY/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform_1/shiftY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform_1/shiftY/useExtMod", 0],
			["float", "/TXDrawTransform_1/shiftY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform_1/shiftY/softMin", -1, -10, 10],
			["float", "/TXDrawTransform_1/shiftY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform_1/rotate", [
			["float", "/TXDrawTransform_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform_1/rotate/useExtMod", 0],
			["float", "/TXDrawTransform_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDrawTransform_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform_1/anchorX", [
			["float", "/TXDrawTransform_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawTransform_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform_1/anchorX/useExtMod", 0],
			["float", "/TXDrawTransform_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawTransform_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform_1/anchorY", [
			["float", "/TXDrawTransform_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawTransform_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform_1/anchorY/useExtMod", 0],
			["float", "/TXDrawTransform_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawTransform_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform_1/scaleX", [
			["float", "/TXDrawTransform_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform_1/scaleX/useExtMod", 0],
			["float", "/TXDrawTransform_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawTransform_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform_1/scaleY", [
			["float", "/TXDrawTransform_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform_1/scaleY/useExtMod", 0],
			["float", "/TXDrawTransform_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawTransform_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDrawTransform_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawCurves_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Points$AutoCurve Start", {arg parameterName; parameterName.containsi("AutoCurveStart");}],
			["Points$Point 1", {arg parameterName; parameterName.containsi("point1");}],
			["Points$Point 2", {arg parameterName; parameterName.containsi("point2");}],
			["Points$Point 3", {arg parameterName; parameterName.containsi("point3");}],
			["Points$Point 4", {arg parameterName; parameterName.containsi("point4");}],
			["Points$Point 5", {arg parameterName; parameterName.containsi("point5");}],
			["Points$Point 6", {arg parameterName; parameterName.containsi("point6");}],
			["Points$Point 7", {arg parameterName; parameterName.containsi("point7");}],
			["Points$Point 8", {arg parameterName; parameterName.containsi("point8");}],
			["Points$AutoCurve End", {arg parameterName; parameterName.containsi("AutoCurveEnd");}],
			["Position Spread", {arg parameterName;
				parameterName.containsi("positionSpreadCurve");
			}],
			["Line Color", {arg parameterName;
				parameterName.containsi("swap")
				|| (parameterName.containsi("lineColor") && (parameterName.containsi("fillColor") == false))}],
			["Fill Color", {arg parameterName;
				parameterName.containsi("fillColor");}],
			["Color Spread", {arg parameterName;
				parameterName.containsi("colorSpreadCurve");
			}],
			["Draw$Active & Max Size", {arg parameterName;
				var drawParameterNames = ["drawActive", "maxWidthHeightRule", "customMaxWidth", "customMaxHeight", "customMaxDepth", "drawWidth", "drawHeight"];
				drawParameterNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Transform, Shift, Scale", {arg parameterName;
				parameterName.containsi("shift")
				|| parameterName.containsi("transformOrder")
				|| parameterName.containsi("scale")
				;}],
			["Draw$Rotate & Anchor", {arg parameterName;
				parameterName.containsi("rotate")
				|| parameterName.containsi("anchor")
				;}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Global";
	}


	initExtraActions { // override
		// arrSlotData = Array.newClear(256).seriesFill(0, 1/255).dup(5); // init to ramp
	}

	extraSubModuleData { // override
		^[arrSlotData];
	}

	loadExtraSubModuleData {arg argData;  // override
		arrSlotData = argData[0];
	}

	getExtraSynthArgSpecs { //  override
		^[
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
	}

	getExtraSectionItems {   // overide
		// Example:
		^[
			["ActionButton", "Curve Editor", {displayOption = "showCurveEditor";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getSectionButtonColour(displayOption == "showXXX")],
			["Spacer", 3],
		];
	}

	getExtraGuiBuildActions {   // overide
		var outArray = [];
		// Example:
		if (displayOption == "showCurveEditor", {
			outArray = [
				["DragDrop", "drag & drop start curve",
					{arg view; View.currentDrag.class == Event;}, {arg view; this.processDrag(View.currentDrag, "");},
					false, 230, 30, {this.getCurveEditorData("");},
					"Points Curve Data",
				],
				["Spacer", 10],
				["DragDrop", "drag & drop end curve",
					{arg view; View.currentDrag.class == Event;}, {arg view; this.processDrag(View.currentDrag, "End");},
					false, 230, 30, {this.getCurveEditorData("End");},
					"Points Curve Data",
				],
				["SpacerLine", 8],
				["ActionButton", "Open start curve in Points Curve Editor",
					{TXPointsCurveEditor.openWithCurveData(this.getCurveEditorData(""));},
					230, TXColor.white, TXColor.sysGuiCol1],
				["Spacer", 10],
				["ActionButton", "Open end curve in Points Curve Editor",
					{TXPointsCurveEditor.openWithCurveData(this.getCurveEditorData("End"));},
					230, TXColor.white, TXColor.sysGuiCol1],
				["Spacer", 3],
			];
		});
		^outArray;
	}

	processDrag {arg dragObject, suffix = "";
		var prefix = "/TXDrawCurves_1/";
		var arrcurveTypes = [\autoCurve, \straightLine, \BezierCP1, \BezierCP1Smooth, \BezierCP1CP2];
		var curveTypeInt;
		var pointSymbols = [
			'acstart',
			'p1d',
			'p2c1', 'p2c2', 'p2d',
			'p3c1', 'p3c2', 'p3d',
			'p4c1', 'p4c2', 'p4d',
			'p5c1', 'p5c2', 'p5d',
			'p6c1', 'p6c2', 'p6d',
			'p7c1', 'p7c2', 'p7d',
			'p8c1', 'p8c2', 'p8d',
			'acend',
		];
		var arrParmNames = [
			"autoCurveStart_control1X", "autoCurveStart_control1Y",
			"point1_drawX", "point1_drawY",
			"point2_control1X", "point2_control1Y", "point2_control2X", "point2_control2Y", "point2_drawX", "point2_drawY",
			"point3_control1X", "point3_control1Y", "point3_control2X", "point3_control2Y", "point3_drawX", "point3_drawY",
			"point4_control1X", "point4_control1Y", "point4_control2X", "point4_control2Y", "point4_drawX", "point4_drawY",
			"point5_control1X", "point5_control1Y", "point5_control2X", "point5_control2Y", "point5_drawX", "point5_drawY",
			"point6_control1X", "point6_control1Y", "point6_control2X", "point6_control2Y", "point6_drawX", "point6_drawY",
			"point7_control1X", "point7_control1Y", "point7_control2X", "point7_control2Y", "point7_drawX", "point7_drawY",
			"point8_control1X", "point8_control1Y", "point8_control2X", "point8_control2Y", "point8_drawX", "point8_drawY",
			"autoCurveEnd_control1X", "autoCurveEnd_control1Y"
		];

		if (dragObject.notNil
			and: {dragObject.class == Event}
			and: {dragObject.label == "Points Curve Data"}, {
				// add data for curve, and adjust min/max values
				dragObject.curveData.pairsDo({arg key, point;
					var holdInd, nameX, nameY, funcSetParm;
					if (pointSymbols.indexOf(key).notNil, {
						funcSetParm = {arg paramName, paramVal;
							dictCurrentParameterVals[paramName.asSymbol] = paramVal;
							this.sendModuleParmToTXV(paramName, paramVal);
						};
						holdInd = 2 * pointSymbols.indexOf(key);
						nameX = prefix++(arrParmNames[holdInd])++suffix;
						nameY = prefix++(arrParmNames[holdInd + 1])++suffix;
						funcSetParm.value((nameX++"/fixedValue"), point.x);
						if (dictCurrentParameterVals[(nameX++"/softMin").asSymbol] > point.x, {
							funcSetParm.value(nameX++"/softMin", point.x);
						});
						if (dictCurrentParameterVals[(nameX++"/softMax").asSymbol] < point.x, {
							funcSetParm.value(nameX++"/softMax", point.x);
						});
						funcSetParm.value(nameY++"/fixedValue", point.y);
						if (dictCurrentParameterVals[(nameY++"/softMin").asSymbol] > point.y, {
							funcSetParm.value(nameY++"/softMin", point.y);
						});
						if (dictCurrentParameterVals[(nameY++"/softMax").asSymbol] < point.y, {
							funcSetParm.value(nameY++"/softMax", point.y);
						});
					});
				});
				// process other curve data
				curveTypeInt = arrcurveTypes.indexOf(dragObject.curveData.curveType);
				if (curveTypeInt != dictCurrentParameterVals['/TXDrawCurves_1/curveType/fixedValue'], {
					// adjust min/max
					if (curveTypeInt < dictCurrentParameterVals['/TXDrawCurves_1/curveType/softMin'], {
						dictCurrentParameterVals['/TXDrawCurves_1/curveType/softMin'] = curveTypeInt;
						this.sendModuleParmToTXV('/TXDrawCurves_1/curveType/softMin', curveTypeInt);
					});
					if (curveTypeInt > dictCurrentParameterVals['/TXDrawCurves_1/curveType/softMax'], {
						dictCurrentParameterVals['/TXDrawCurves_1/curveType/softMax'] = curveTypeInt;
						this.sendModuleParmToTXV('/TXDrawCurves_1/curveType/softMax', curveTypeInt);
					});
					dictCurrentParameterVals['/TXDrawCurves_1/curveType/fixedValue'] = curveTypeInt;
						this.sendModuleParmToTXV('/TXDrawCurves_1/curveType/fixedValue', curveTypeInt);
				});
				if (dragObject.curveData.numPoints !=
					dictCurrentParameterVals['/TXDrawCurves_1/numPoints/fixedValue'], {
						// adjust min/max
						if (dragObject.curveData.numPoints < dictCurrentParameterVals['/TXDrawCurves_1/numPoints/softMin'], {
							dictCurrentParameterVals['/TXDrawCurves_1/numPoints/softMin'] = dragObject.curveData.numPoints;
						this.sendModuleParmToTXV('/TXDrawCurves_1/numPoints/softMin', dragObject.curveData.numPoints);
						});
						if (dragObject.curveData.numPoints > dictCurrentParameterVals['/TXDrawCurves_1/numPoints/softMax'], {
							dictCurrentParameterVals['/TXDrawCurves_1/numPoints/softMax'] = dragObject.curveData.numPoints;
						this.sendModuleParmToTXV('/TXDrawCurves_1/numPoints/softMax', dragObject.curveData.numPoints);
						});
						dictCurrentParameterVals['/TXDrawCurves_1/numPoints/fixedValue'] = dragObject.curveData.numPoints;
						this.sendModuleParmToTXV('/TXDrawCurves_1/numPoints/fixedValue', dragObject.curveData.numPoints);
				});
				if (dragObject.curveData.autoClose !=
					dictCurrentParameterVals['/TXDrawCurves_1/autoClose/fixedValue'].asBoolean, {
						dictCurrentParameterVals['/TXDrawCurves_1/autoClose/fixedValue'] =
						dragObject.curveData.autoClose.binaryValue;
						this.sendModuleParmToTXV('/TXDrawCurves_1/autoClose/fixedValue',
							dragObject.curveData.autoClose.binaryValue);
				});
				if (dragObject.curveData.usePoint1ForAutoCurveStart !=
					dictCurrentParameterVals['/TXDrawCurves_1/usePoint1ForAutoCurveStart'].asBoolean, {
						dictCurrentParameterVals['/TXDrawCurves_1/usePoint1ForAutoCurveStart'] =
						dragObject.curveData.usePoint1ForAutoCurveStart.binaryValue;
						this.sendModuleParmToTXV('/TXDrawCurves_1/usePoint1ForAutoCurveStart',
							dragObject.curveData.usePoint1ForAutoCurveStart.binaryValue
						);
				});
				if (dragObject.curveData.useLastPointForAutoCurveEnd !=
					dictCurrentParameterVals['/TXDrawCurves_1/useLastPointForAutoCurveEnd'].asBoolean, {
						dictCurrentParameterVals['/TXDrawCurves_1/useLastPointForAutoCurveEnd'] =
						dragObject.curveData.useLastPointForAutoCurveEnd.binaryValue;
						this.sendModuleParmToTXV('/TXDrawCurves_1/useLastPointForAutoCurveEnd',
							dragObject.curveData.useLastPointForAutoCurveEnd.binaryValue);
				});
		});
	}

	getCurveEditorData {arg suffix = ""; // "" or "End"
		var arrCurveTypes = [\autoCurve, \straightLine, \BezierCP1, \BezierCP1Smooth, \BezierCP1CP2];
		var curveTypeInt;
		var outData = ();
		outData.label = "Points Curve Data";
		// add curve data
		outData.curveData = this.buildPointData(suffix); // suffix: "" or "End"
		curveTypeInt = dictCurrentParameterVals['/TXDrawCurves_1/curveType/fixedValue'].asInteger;
		outData.curveData.curveType = arrCurveTypes[curveTypeInt];
		outData.curveData.numPoints = dictCurrentParameterVals['/TXDrawCurves_1/numPoints/fixedValue'];
		outData.curveData.autoClose = dictCurrentParameterVals[
			'/TXDrawCurves_1/autoClose/fixedValue'].asBoolean;
		outData.curveData.usePoint1ForAutoCurveStart = dictCurrentParameterVals[
			'/TXDrawCurves_1/usePoint1ForAutoCurveStart'].asBoolean;
		outData.curveData.useLastPointForAutoCurveEnd = dictCurrentParameterVals[
			'/TXDrawCurves_1/useLastPointForAutoCurveEnd'].asBoolean;
		// add options
		outData.options = ();
		/*
		// not used now:
		outData.options.showCurveEditOptions = true;
		outData.options.curveEditOption = 2;
		// 0, show start curve only
		// 1, show end curve only
		// 2, show start curve with non-editable end curve behind it
		// 3, show end curve with non-editable main curve behind it
		*/
		^outData;
	}

	buildPointData {arg suffix = "";
		var dataEvent = ();
		var prefix = "/TXDrawCurves_1/";
		var pointSymbols = [
			'acstart',
			'p1d',
			'p2c1', 'p2c2', 'p2d',
			'p3c1', 'p3c2', 'p3d',
			'p4c1', 'p4c2', 'p4d',
			'p5c1', 'p5c2', 'p5d',
			'p6c1', 'p6c2', 'p6d',
			'p7c1', 'p7c2', 'p7d',
			'p8c1', 'p8c2', 'p8d',
			'acend',
		];
		var arrParmNames = [
			"autoCurveStart_control1X", "autoCurveStart_control1Y",
			"point1_drawX", "point1_drawY",
			"point2_control1X", "point2_control1Y", "point2_control2X", "point2_control2Y", "point2_drawX", "point2_drawY",
			"point3_control1X", "point3_control1Y", "point3_control2X", "point3_control2Y", "point3_drawX", "point3_drawY",
			"point4_control1X", "point4_control1Y", "point4_control2X", "point4_control2Y", "point4_drawX", "point4_drawY",
			"point5_control1X", "point5_control1Y", "point5_control2X", "point5_control2Y", "point5_drawX", "point5_drawY",
			"point6_control1X", "point6_control1Y", "point6_control2X", "point6_control2Y", "point6_drawX", "point6_drawY",
			"point7_control1X", "point7_control1Y", "point7_control2X", "point7_control2Y", "point7_drawX", "point7_drawY",
			"point8_control1X", "point8_control1Y", "point8_control2X", "point8_control2Y", "point8_drawX", "point8_drawY",
			"autoCurveEnd_control1X", "autoCurveEnd_control1Y"
		];
		arrParmNames.pairsDo({arg nameX, nameY, i;
			var holdPoint = Point(
				dictCurrentParameterVals[(prefix++nameX++suffix++"/fixedValue").asSymbol],
				dictCurrentParameterVals[(prefix++nameY++suffix++"/fixedValue").asSymbol]);

			dataEvent[pointSymbols[i div: 2]] = holdPoint;
		});
		^dataEvent;
	}

}


