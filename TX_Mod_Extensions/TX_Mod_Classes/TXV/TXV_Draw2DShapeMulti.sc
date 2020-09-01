// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Draw2DShapeMulti : TXV_Module {
	classvar <defaultName = "TXV Shape2D Multi";
	classvar <>arrInstances;
	var <>arrSlotData;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."shapeType/fixedValue":
	// , ["Circle (width, circleResolution)", "Ellipse (width, height, circleResolution)", "Square (width)", "Rectangle (width, height)", "Round Rectangle (width, height, roundness, circleResolution)", "Star (width, height, circleResolution)", "Triangle 1 (Isosceles) (width, height)", "Triangle 2 (right) (width, height)", "Diamond (width, height)", "Crescent (width, height, thickness, circleResolution)", "Ring (width, thickness, circleResolution)"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDraw2DShapeMulti_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDraw2DShapeMulti_1/drawActive", [
			["bool_int", "/TXDraw2DShapeMulti_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDraw2DShapeMulti_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/drawActive/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDraw2DShapeMulti_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDraw2DShapeMulti_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDraw2DShapeMulti_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXDraw2DShapeMulti_1/shapeType", [
			["int", "/TXDraw2DShapeMulti_1/shapeType/fixedValue", 0, 0, 10, ["Circle (width, circleResolution)", "Ellipse (width, height, circleResolution)", "Square (width)","Rectangle (width, height)", "Round Rectangle (width, height, roundness, circleResolution)", "Star (width, height, circleResolution)", "Triangle 1 (Isosceles) (width, height)", "Triangle 2 (right) (width, height)", "Diamond (width, height)", "Crescent (width, height, thickness, circleResolution)", "Ring (width, thickness, circleResolution)"]],
			["float", "/TXDraw2DShapeMulti_1/shapeType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/shapeType/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/shapeType/extModValue", 0, 0, 1],
			["int", "/TXDraw2DShapeMulti_1/shapeType/softMin", 0, 0, 10],
			["int", "/TXDraw2DShapeMulti_1/shapeType/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupInt", "/TXDraw2DShapeMulti_1/numCopies", [
			["int", "/TXDraw2DShapeMulti_1/numCopies/fixedValue", 6, 1, 20],
			["float", "/TXDraw2DShapeMulti_1/numCopies/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/numCopies/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/numCopies/extModValue", 0, 0, 1],
			["int", "/TXDraw2DShapeMulti_1/numCopies/softMin", 1, 0, 1000],
			["int", "/TXDraw2DShapeMulti_1/numCopies/softMax", 20, 0, 1000],
		]],
		[ "modParameterGroupInt", "/TXDraw2DShapeMulti_1/lineWidth", [
			["int", "/TXDraw2DShapeMulti_1/lineWidth/fixedValue", 2, 0, 10],
			["float", "/TXDraw2DShapeMulti_1/lineWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineWidth/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineWidth/extModValue", 0, 0, 1],
			["int", "/TXDraw2DShapeMulti_1/lineWidth/softMin", 0, 0, 10],
			["int", "/TXDraw2DShapeMulti_1/lineWidth/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupBool", "/TXDraw2DShapeMulti_1/fillShape", [
			["bool_int", "/TXDraw2DShapeMulti_1/fillShape/fixedValue", 1],
			["bool_int", "/TXDraw2DShapeMulti_1/fillShape/fixedModMix", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/fillShape/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/fillShape/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/width", [
			["float", "/TXDraw2DShapeMulti_1/width/fixedValue", 0.05, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/width/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/width/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/width/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/width/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/width/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/widthEnd", [
			["float", "/TXDraw2DShapeMulti_1/widthEnd/fixedValue", 0.05, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/widthEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/widthEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/widthEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/widthEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/widthEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/widthEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/height", [
			["float", "/TXDraw2DShapeMulti_1/height/fixedValue", 0.1, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/height/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/height/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/height/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/height/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/height/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/heightEnd", [
			["float", "/TXDraw2DShapeMulti_1/heightEnd/fixedValue", 0.1, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/heightEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/heightEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/heightEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/heightEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/heightEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/heightEndIgnore", 1],
		["bool_int", "/TXDraw2DShapeMulti_1/useMaxWidthToScaleHeight", 0],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/roundness", [
			["float", "/TXDraw2DShapeMulti_1/roundness/fixedValue", 0.2, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/roundness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/roundness/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/roundness/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/roundness/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/roundness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/roundnessEnd", [
			["float", "/TXDraw2DShapeMulti_1/roundnessEnd/fixedValue", 0.2, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/roundnessEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/roundnessEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/roundnessEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/roundnessEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/roundnessEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/roundnessEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/thickness", [
			["float", "/TXDraw2DShapeMulti_1/thickness/fixedValue", 0.2, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/thickness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/thickness/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/thickness/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/thickness/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/thickness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/thicknessEnd", [
			["float", "/TXDraw2DShapeMulti_1/thicknessEnd/fixedValue", 0.2, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/thicknessEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/thicknessEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/thicknessEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/thicknessEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/thicknessEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/thicknessEndIgnore", 1],
		[ "modParameterGroupInt", "/TXDraw2DShapeMulti_1/circleResolution", [
			["int", "/TXDraw2DShapeMulti_1/circleResolution/fixedValue", 32, 3, 128],
			["float", "/TXDraw2DShapeMulti_1/circleResolution/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/circleResolution/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/circleResolution/extModValue", 0, 0, 1],
			["int", "/TXDraw2DShapeMulti_1/circleResolution/softMin", 3, 3, 1024],
			["int", "/TXDraw2DShapeMulti_1/circleResolution/softMax", 128, 3, 1024],
		]],
		["float256", "/TXDraw2DShapeMulti_1/sizeSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDraw2DShapeMulti_1/sizeSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/sizeSpreadCurveMorph", [
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/sizeSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/sizeSpreadCurvePhase", [
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/sizeSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/sizeSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorHue", [
			["float", "/TXDraw2DShapeMulti_1/colorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorHue/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorHue/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHue/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorHueEnd", [
			["float", "/TXDraw2DShapeMulti_1/colorHueEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHueEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorHueEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorHueEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHueEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHueEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/colorHueEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorHueRotate", [
			["float", "/TXDraw2DShapeMulti_1/colorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorHueRotate/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorSaturation", [
			["float", "/TXDraw2DShapeMulti_1/colorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorSaturation/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSaturation/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorSaturationEnd", [
			["float", "/TXDraw2DShapeMulti_1/colorSaturationEnd/fixedValue", 0.6, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSaturationEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorSaturationEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorSaturationEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSaturationEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSaturationEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/colorSaturationEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorBrightness", [
			["float", "/TXDraw2DShapeMulti_1/colorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorBrightness/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorBrightness/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorBrightnessEnd", [
			["float", "/TXDraw2DShapeMulti_1/colorBrightnessEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorBrightnessEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorBrightnessEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorBrightnessEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorBrightnessEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorBrightnessEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/colorBrightnessEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorAlpha", [
			["float", "/TXDraw2DShapeMulti_1/colorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorAlpha/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorAlpha/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorAlpha/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorAlphaEnd", [
			["float", "/TXDraw2DShapeMulti_1/colorAlphaEnd/fixedValue", 1, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorAlphaEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorAlphaEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorAlphaEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorAlphaEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorAlphaEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/colorAlphaEndIgnore", 1],
		[ "modParameterGroupBool", "/TXDraw2DShapeMulti_1/colorHSBAsRGB", [
			["bool_int", "/TXDraw2DShapeMulti_1/colorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/colorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/colorHSBAsRGB/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDraw2DShapeMulti_1/useFillColorForLineColor", [
			["bool_int", "/TXDraw2DShapeMulti_1/useFillColorForLineColor/fixedValue", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/useFillColorForLineColor/fixedModMix", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/useFillColorForLineColor/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/useFillColorForLineColor/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDraw2DShapeMulti_1/swapFillColorAndLineColor", [
			["bool_int", "/TXDraw2DShapeMulti_1/swapFillColorAndLineColor/fixedValue", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/swapFillColorAndLineColor/fixedModMix", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/swapFillColorAndLineColor/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/swapFillColorAndLineColor/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorHue", [
			["float", "/TXDraw2DShapeMulti_1/lineColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorHue/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorHue/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHue/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorHueEnd", [
			["float", "/TXDraw2DShapeMulti_1/lineColorHueEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHueEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorHueEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorHueEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHueEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHueEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/lineColorHueEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorHueRotate", [
			["float", "/TXDraw2DShapeMulti_1/lineColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorHueRotate/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorSaturation", [
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorSaturation/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturation/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorSaturationEnd", [
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturationEnd/fixedValue", 0.6, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturationEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorSaturationEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturationEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturationEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorSaturationEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/lineColorSaturationEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorBrightness", [
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorBrightness/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightness/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorBrightnessEnd", [
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightnessEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightnessEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorBrightnessEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightnessEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightnessEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorBrightnessEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/lineColorBrightnessEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorAlpha", [
			["float", "/TXDraw2DShapeMulti_1/lineColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorAlpha/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorAlpha/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/lineColorAlphaEnd", [
			["float", "/TXDraw2DShapeMulti_1/lineColorAlphaEnd/fixedValue", 1, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorAlphaEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorAlphaEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorAlphaEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorAlphaEnd/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/lineColorAlphaEnd/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/lineColorAlphaEndIgnore", 1],
		[ "modParameterGroupBool", "/TXDraw2DShapeMulti_1/lineColorHSBAsRGB", [
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDraw2DShapeMulti_1/lineColorHSBAsRGB/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/lineColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["float256", "/TXDraw2DShapeMulti_1/colorSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDraw2DShapeMulti_1/colorSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorSpreadCurveMorph", [
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/colorSpreadCurvePhase", [
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/colorSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/colorSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/centrePositionX", [
			["float", "/TXDraw2DShapeMulti_1/centrePositionX/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/centrePositionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/centrePositionX/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/centrePositionX/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/centrePositionX/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/centrePositionX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/centrePositionY", [
			["float", "/TXDraw2DShapeMulti_1/centrePositionY/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/centrePositionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/centrePositionY/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/centrePositionY/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/centrePositionY/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/centrePositionY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/positionX", [
			["float", "/TXDraw2DShapeMulti_1/positionX/fixedValue", -0.3, -1, 1],
			["float", "/TXDraw2DShapeMulti_1/positionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/positionX/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/positionX/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionX/softMin", -1, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/positionX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/positionXEnd", [
			["float", "/TXDraw2DShapeMulti_1/positionXEnd/fixedValue", 0.3, -1, 1],
			["float", "/TXDraw2DShapeMulti_1/positionXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/positionXEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/positionXEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionXEnd/softMin", -1, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/positionXEnd/softMax", 1, -10, 10],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/positionXEndIgnore", 0],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/positionY", [
			["float", "/TXDraw2DShapeMulti_1/positionY/fixedValue", 0, -1, 1],
			["float", "/TXDraw2DShapeMulti_1/positionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/positionY/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/positionY/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionY/softMin", -1, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/positionY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/positionYEnd", [
			["float", "/TXDraw2DShapeMulti_1/positionYEnd/fixedValue", 0, -1, 1],
			["float", "/TXDraw2DShapeMulti_1/positionYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/positionYEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/positionYEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionYEnd/softMin", -1, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/positionYEnd/softMax", 1, -10, 10],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/positionYEndIgnore", 1],
		["float256", "/TXDraw2DShapeMulti_1/positionXSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDraw2DShapeMulti_1/positionXSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/positionXSpreadCurveMorph", [
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/positionXSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/positionXSpreadCurvePhase", [
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/positionXSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionXSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/usePosXSpreadCurveForPosY", 0],
		["float256", "/TXDraw2DShapeMulti_1/positionYSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDraw2DShapeMulti_1/positionYSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/positionYSpreadCurveMorph", [
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/positionYSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/positionYSpreadCurvePhase", [
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/positionYSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/positionYSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/rotate", [
			["float", "/TXDraw2DShapeMulti_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDraw2DShapeMulti_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/rotate/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDraw2DShapeMulti_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/rotateEnd", [
			["float", "/TXDraw2DShapeMulti_1/rotateEnd/fixedValue", 0, -360, 360],
			["float", "/TXDraw2DShapeMulti_1/rotateEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/rotateEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/rotateEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateEnd/softMin", -360, -360, 360],
			["float", "/TXDraw2DShapeMulti_1/rotateEnd/softMax", 360, -360, 360],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/rotateEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/rotateMultiply", [
			["float", "/TXDraw2DShapeMulti_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDraw2DShapeMulti_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDraw2DShapeMulti_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/rotateMultiplyEnd", [
			["float", "/TXDraw2DShapeMulti_1/rotateMultiplyEnd/fixedValue", 1, 0, 10],
			["float", "/TXDraw2DShapeMulti_1/rotateMultiplyEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/rotateMultiplyEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/rotateMultiplyEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateMultiplyEnd/softMin", 0, 0, 10],
			["float", "/TXDraw2DShapeMulti_1/rotateMultiplyEnd/softMax", 10, 0, 10],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/rotateMultiplyEndIgnore", 1],
		["float256", "/TXDraw2DShapeMulti_1/rotateSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDraw2DShapeMulti_1/rotateSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/rotateSpreadCurveMorph", [
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/rotateSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/rotateSpreadCurvePhase", [
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/rotateSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/rotateSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/anchorX", [
			["float", "/TXDraw2DShapeMulti_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/anchorX/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorX/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/anchorX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/anchorXEnd", [
			["float", "/TXDraw2DShapeMulti_1/anchorXEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/anchorXEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/anchorXEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorXEnd/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/anchorXEnd/softMax", 1, -10, 10],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/anchorXEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/anchorY", [
			["float", "/TXDraw2DShapeMulti_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/anchorY/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorY/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/anchorY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/anchorYEnd", [
			["float", "/TXDraw2DShapeMulti_1/anchorYEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/anchorYEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/anchorYEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorYEnd/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/anchorYEnd/softMax", 1, -10, 10],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/anchorYEndIgnore", 1],
		["float256", "/TXDraw2DShapeMulti_1/anchorSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDraw2DShapeMulti_1/anchorSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/anchorSpreadCurveMorph", [
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/anchorSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/anchorSpreadCurvePhase", [
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/anchorSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/anchorSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/scaleX", [
			["float", "/TXDraw2DShapeMulti_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDraw2DShapeMulti_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/scaleX/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/scaleXEnd", [
			["float", "/TXDraw2DShapeMulti_1/scaleXEnd/fixedValue", 1, 0, 3],
			["float", "/TXDraw2DShapeMulti_1/scaleXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/scaleXEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/scaleXEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleXEnd/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/scaleXEnd/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/scaleXEndIgnore", 1],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/scaleY", [
			["float", "/TXDraw2DShapeMulti_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDraw2DShapeMulti_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/scaleY/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/scaleY/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/scaleYEnd", [
			["float", "/TXDraw2DShapeMulti_1/scaleYEnd/fixedValue", 1, 0, 3],
			["float", "/TXDraw2DShapeMulti_1/scaleYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/scaleYEnd/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/scaleYEnd/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleYEnd/softMin", 0, -10, 10],
			["float", "/TXDraw2DShapeMulti_1/scaleYEnd/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/scaleYEndIgnore", 1],
		["bool_int", "/TXDraw2DShapeMulti_1/useScaleXForScaleY", 0],
		["float256", "/TXDraw2DShapeMulti_1/scaleXSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDraw2DShapeMulti_1/scaleXSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/scaleXSpreadCurveMorph", [
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/scaleXSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/scaleXSpreadCurvePhase", [
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/scaleXSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleXSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShapeMulti_1/useScaleXSpreadCurveForScaleY", 0],
		["float256", "/TXDraw2DShapeMulti_1/scaleYSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDraw2DShapeMulti_1/scaleYSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/scaleYSpreadCurveMorph", [
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/scaleYSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShapeMulti_1/scaleYSpreadCurvePhase", [
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShapeMulti_1/scaleYSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDraw2DShapeMulti_1/scaleYSpreadCurvePhase/softMax", 1, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDraw2DShapeMulti_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		var fillColorNames = ["colorHue", "colorHueEnd", "colorHueEndIgnore", "colorHueRotate", "colorSaturation", "colorSaturationEnd", "colorSaturationEndIgnore", "colorBrightness", "colorBrightnessEnd", "colorBrightnessEndIgnore", "colorAlpha", "colorAlphaEnd", "colorAlphaEndIgnore", "colorHSBAsRGB", ];
		var outlineNames = ["useFillColorForLineColor", "swapFillColorAndLineColor", "lineColorHue", "lineColorHueEnd", "lineColorHueEndIgnore", "lineColorHueRotate", "lineColorSaturation", "lineColorSaturationEnd", "lineColorSaturationEndIgnore", "lineColorBrightness", "lineColorBrightnessEnd", "lineColorBrightnessEndIgnore", "lineColorAlpha", "lineColorAlphaEnd", "lineColorAlphaEndIgnore", "lineColorHSBAsRGB", ];
		^[
			// format: ["Example Name", nameValidityFunction],
			["Size$Width & Height", {arg parameterName;
				var arrNames = ["width", "widthEnd", "widthEndIgnore", "height", "heightEnd",
					"heightEndIgnore", "useMaxWidthToScaleHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Size$Size Spread", {arg parameterName;
				parameterName.containsi("sizeSpreadCurve");
			}],
			["Colors$Fill Color", {arg parameterName;
				fillColorNames.indexOfEqual(parameterName).notNil;
			}],
			["Colors$Outline Color", {arg parameterName;
				outlineNames.indexOfEqual(parameterName).notNil;
			}],
			["Colors$Color Spread", {arg parameterName;
				parameterName.containsi("colorSpreadCurve");
			}],
			["Position$Position XY", {arg parameterName;
				parameterName.containsi("position")
				&& not(parameterName.containsi("SpreadCurve"))
			}],
			["Position$Position X Spread", {arg parameterName;
				parameterName.containsi("positionXSpreadCurve")
				&& not(parameterName.containsi("usePosXSpreadCurveForPosY"))
			}],
			["Position$Position Y Spread", {arg parameterName;
				parameterName.containsi("positionYSpreadCurve")
				|| parameterName.containsi("usePosXSpreadCurveForPosY")
			}],
			["Scale$Scale XY", {arg parameterName;
				parameterName.containsi("scale")
				&& not(parameterName.containsi("useMaxWidthToScaleHeight"))
				&& not(parameterName.containsi("SpreadCurve"))
			}],
			["Scale$Scale X Spread", {arg parameterName;
				parameterName.containsi("scaleXSpreadCurve")
				&& not(parameterName.containsi("useScaleXSpreadCurveForScaleY"))
			}],
			["Scale$Scale Y Spread", {arg parameterName;
				parameterName.containsi("scaleYSpreadCurve")
				||  parameterName.containsi("useScaleXSpreadCurveForScaleY");
			}],
			["Anchor$Anchor XY", {arg parameterName;
				parameterName.containsi("anchor")
				&& not(parameterName.containsi("anchorSpreadCurve"))
			}],
			["Anchor$Anchor Spread", {arg parameterName;
				parameterName.containsi("anchorSpreadCurve");
			}],
			["Rotate$Rotate & Multiply", {arg parameterName;
				parameterName.containsi("rotate")
				&& not(parameterName.containsi("rotateSpreadCurve"))
			}],
			["Rotate$Rotate Spread", {arg parameterName;
				parameterName.containsi("rotateSpreadCurve");
			}],
			["Draw Active & Max Size", {arg parameterName;
				var arrNames = ["drawActive", "maxWidthHeightRule", "customMaxWidth", "customMaxHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}
	getDefaultParamSectionName {  // override
		^"Shape";
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

}


