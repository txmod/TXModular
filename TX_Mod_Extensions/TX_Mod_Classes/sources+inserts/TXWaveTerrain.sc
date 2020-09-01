// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWaveTerrain : TXModuleBase {

	classvar <>classData;

	var	arrOrbitPresets;
	var	arrTerrainPresets;
	var terrainCodeString;
	var orbitCodeString;
	var displayOption;
	var terrainArray;
	var importedImageTerrain;
	var importedImageFilename;
	var builderTerrain;
	var builderWTData;
	var terrainImage;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Wave Terrain";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Frequency", 1, "modFreq", 0],
			["Modify 1", 1, "modChange1", 0],
			["Modify 2", 1, "modChange2", 0],
			["Modify 3", 1, "modChange3", 0],
			["Modify 4", 1, "modChange4", 0],
			["Out Level", 1, "modOutLevel", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.terrainWidth = 256; // used for wave terrain width & height
		//
		// NOTE - terrainWidth of 512 or 1024 sound slightly better tho still lo-fi, but take too long to load
		//
		classData.arrBufferSpecs = [ ["bufnumTerrain", (classData.terrainWidth * classData.terrainWidth), 1] ];
		classData.guiWidth = 680;
		classData.freqSpec = ControlSpec(5, 20000, \exponential);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	create presets
		arrTerrainPresets =[
			[	"Default",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Default
// {arg x, y; (x-y) * (x-1) * (x+1) * (y-1) * (y+1); }
{arg x, y; 2*(((x)**3) + ((abs(sin(10*y)))**(1/3)))-1; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 1",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 1
{arg x, y; 2*(((x)**2) + ((abs(sin(10*y)))**(1/3)))-1; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 2",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 2
{arg x, y; (((cos(5*x+1.7))**3) - ((abs(sin(23*y)))**(1/3))); }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 3",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 3
{arg x, y; (((1.3*(cos(8.1*x*y+1.7))**2) - ((abs(sin(4.9*y)))**(1/2)))); }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 4",
				{this.updateRangeFunctionOption(3); // Wrap
				this.evaluateTerrainCode(
					"// Terrain - Experimental 4
{arg x,y; (0.5 * cos(x + (8 * y))) - (0.5 * sin(2*x)**2) + (sin(10*y + x*x));}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 5",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 5
{arg x,y; (0.5 * cos(x + (-8 * y*y*y))) + (0.5 * cos(y + (-0.5 * x*y*y)))
		- (0.5 * sin(2*x)**2) + (sin(10*y + x*x));}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 6",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 6
{arg x,y; (-1.8*(sin(8.1*x*x+16.7*y))) + (((cos(5*x*y+1.7))**3) - ((abs(sin(2*x*y)))**(1/3)))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 7 with noise",
				{this.updateRangeFunctionOption(3); // Wrap
				this.evaluateTerrainCode(
					"// Terrain - Experimental 7
{arg x,y; (((2.3*(cos(rrand(1,2)*x*(3*y)+1.7))**2) - ((abs(sin(rrand(0.2,1.9)*y)))**(1/6))))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 8 with noise",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 8
{arg x,y; (((2.6*(cos(rrand(1,2)*x*y+1.7))**2) - ((abs(sin(rrand(0.2,1.9)*y)))**(1/6))))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 9",
				{this.updateRangeFunctionOption(4); // Clip
				this.evaluateTerrainCode(
					"// Terrain - Experimental 9
{arg x,y; (((1.3*(cos(8.1*x*y+1.7))**2) - ((abs(sin(0.9*y)))**(1/2))))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 10",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 10
{arg x,y; (0.8*(sin(18.1*x*x+11.7*y))) + (((1.3*(cos(8.1*x*y+1.7))**2) - ((abs(sin(0.9*y)))**(1/2))))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 11",
				{this.updateRangeFunctionOption(3); // Wrap
				this.evaluateTerrainCode(
					"// Terrain - Experimental 11
{arg x,y; (-0.8*(sin(18.1*x*x+11.7*y))) - (((-0.7*(cos(8.1*x*y-1.7))**2) + ((abs(sin(0.9*y)))**(1/3))))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 12",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 12
{arg x,y; (-1.8* y *(sin(33*x*x*x+11.7*y))) + (((0.7*(sin(99*x*y*y-1.7))**2) + ((abs(cos(0.8*y)))**(1/4))))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 13",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 13
{arg x,y; (2.8* y *(sin(33*x*x*x+11.7*y))) + (((1.7*(sin(22*x*x*y-1.7))**2) + ((abs(sin(0.8*y*x)))**(1/4))))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 14",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 14
{arg x,y; (((1.3*(cos(8.1*x*y+1.7))**2) - ((abs(sin(3.9*y)))**(1/2))))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 15",
				{this.updateRangeFunctionOption(2); // Fold
				this.evaluateTerrainCode(
					"// Terrain - Experimental 15
{arg x,y; (0.8*(sin(18.1*x*y+11.7*y))) + (((1.3*(sin(8.1*x*y+1.7))**2) - ((abs(sin(2.9*y)))**2)))}");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Experimental 16",
				{this.updateRangeFunctionOption(3); // Wrap
				this.evaluateTerrainCode(
					"// Terrain - Experimental 16
{arg x,y; (-0.8*(sin(8.1*x*x+16.7*y))) - (((1.7*(cos(8.1*x*y+1.7))**3) + ((abs(cos(4.9*y)))**(1/3))))}");
				this.buildGuiSpecArray; system.showView;},
			],
		];
		arrOrbitPresets =[
			[	"Ellipse",
				{this.evaluateOrbitCode(
					"// Orbit - Ellipse
// uses Frequency & Modify 1 & 2
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = mod1.max(0.001) * cos(t);
y = mod2.max(0.001) * sin(t);
[x,y]; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Astroid",
				{this.evaluateOrbitCode(
					"// Orbit - Astroid
// uses Frequency & Modify 1 & 2
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = mod1.max(0.001) * cos(cos(cos(t)));
y = mod2.max(0.001) * sin(sin(sin(t)));
[x,y]; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Spiral",
				{this.evaluateOrbitCode(
					"// Orbit - Spiral
// uses Frequency & Modify 1
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = mod1.max(0.001) * t * cos(t);
y = mod1.max(0.001) * t * sin(t);
[x,y]; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Mix 1",
				{this.evaluateOrbitCode(
					"// Orbit - Mix 1
// uses Frequency & Modify 1 & 2 & 3 & 4
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = mod1.max(0.001) * sin(mod3 * t);
y = mod2.max(0.001) * sin(mod4 * t);
[x,y]; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Mix 2",
				{this.evaluateOrbitCode(
					"// Orbit - Mix 2
// uses Frequency & Modify 1 & 2 & 3 & 4
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = mod1.max(0.001) * cos(mod3 * t);
y = mod2.max(0.001) * cos(mod4 * t);
[x,y]; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Mix 3",
				{this.evaluateOrbitCode(
					"// Orbit - Mix 3
// uses Frequency & Modify 1
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = mod1.max(0.001) * sin(t);
y = (sin(t) + 1) * tan (t);
[x,y]; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Mix 4",
				{this.evaluateOrbitCode(
					"// Orbit - Mix 4
// uses Frequency & Modify 1 & 2 & 3 & 4
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = mod1.clip(0.02,0.99) * sin(mod3.clip(0.01,0.99) * t)
* cos(mod4.clip(0.01,0.99) * t);
y = mod2.clip(0.02,0.99) * sin(mod3.clip(0.01,0.99) * t)
* sin(mod4.clip(0.01,0.99) * t);
[x,y]; }");
				this.buildGuiSpecArray; system.showView;},
			],
			[	"Ellipse with x-y shift",
				{this.evaluateOrbitCode(
					"// Orbit - Ellipse with x-y shift (mod3&4)
// uses Frequency & Modify 1 & 2 & 3 & 4
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = (mod1.max(0.001) * cos(t)) - 0.5 + mod3;
y = (mod2.max(0.001) * sin(t)) - 0.5 + mod4;
[x,y]; }");
				this.buildGuiSpecArray; system.showView;},
			],

			/*  Chaotic orbits */
			["Chaotic - Logistic map - linear smoothing",

				{this.evaluateOrbitCode(
					"// Orbit - Chaotic - Logistic map - linear smoothing
// uses Frequency & Modify 1
{arg freq, mod1;
var r = 3.79 ! 2;
freq = freq * 0.25;
Ramp.ar(Logistic.ar(r, freq, mul: 2, add: -1), freq.reciprocal);
}");
				this.buildGuiSpecArray; system.showView;}
			],
			["Chaotic - Logistic map - exponential smoothing",

				{this.evaluateOrbitCode(
					"// Orbit - Chaotic - Logistic map - exponential smoothing
// uses Frequency & Modify 1
{arg freq, mod1;
var r = 3.79 ! 2;
freq = freq * 0.25;
Lag.ar(Logistic.ar(r, freq, mul: 2, add: -1), freq.reciprocal);
}");
				this.buildGuiSpecArray; system.showView;}
			],
			["Chaotic - Standard map - linear smoothing",

				{this.evaluateOrbitCode(
					"// Orbit - Chaotic - Standard map - linear smoothing
// uses Frequency & Modify 1
{arg freq, mod1;
var r;
freq = freq * 0.25;
r = LFNoise2.ar((freq * 0.02).max(0.1), 1.5, 0.9) ! 2;
StandardL.ar(freq, r);
}");
				this.buildGuiSpecArray; system.showView;}
			],
			["Chaotic - Quadratic map - linear smoothing",

				{this.evaluateOrbitCode(
					"// Orbit - Chaotic - Quadratic map - linear smoothing
// uses Frequency & Modify 1
{arg freq;
var r = 3.79 ! 2;
QuadL.ar(freq * 0.25, r.neg, r, 0, 0.1).madd(2, -1);
}");
				this.buildGuiSpecArray; system.showView;}
			],
			["Chaotic - Quadratic map - cubic smoothing",

				{this.evaluateOrbitCode(
					"// Orbit - Chaotic - Quadratic map - cubic smoothing
// uses Frequency & Modify 1
{arg freq;
var r = 3.79 ! 2;
QuadC.ar(freq * 0.25, r.neg, r, 0, 0.1).madd(2, -1);
}");
				this.buildGuiSpecArray; system.showView;}
			],
		];
		//	set  class specific instance variables
		displayOption = "showOrbitControls";

		terrainCodeString = "// Terrain - Default
{arg x,y; (0.5 * cos(x + (8 * y))) - (0.5 * sin(2*x)**2) + (sin(10*y + x*x));}";

		orbitCodeString = "// Orbit - Ellipse
// uses Frequency & Modify 1 & 2
{ arg freq, mod1, mod2, mod3, mod4;
var x, y, t;
t = Saw.ar(freq).range(0, 2pi);
x = mod1.max(0.001) * cos(t);
y = mod2.max(0.001) * sin(t);
[x,y]; }";

		arrSynthArgSpecs = [
			["out", 0, 0],
			["bufnumTerrain", 0, \ir],
			["freq", 0.5, defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["change1", 0.5, defLagTime],
			["change1Min", 0, defLagTime],
			["change1Max", 1, defLagTime],
			["change2", 0.5, defLagTime],
			["change2Min", 0, defLagTime],
			["change2Max", 1, defLagTime],
			["change3", 0.5, defLagTime],
			["change3Min", 0, defLagTime],
			["change3Max", 1, defLagTime],
			["change4", 0.5, defLagTime],
			["change4Min", 0, defLagTime],
			["change4Max", 1, defLagTime],
			["outLevel", 1, defLagTime],
			["modFreq", 0, defLagTime],
			["modChange1", 0, defLagTime],
			["modChange2", 0, defLagTime],
			["modChange3", 0, defLagTime],
			["modChange4", 0, defLagTime],
			["modOutLevel", 0, defLagTime],
		];
		arrOptions = [0, 0, 0, 0];
		arrOptionData = [
			[  // range functions
				// ["Normalise - bipolar (default)", {arg inputArray;
				// 	var maxVal = inputArray.abs.reduce({ |a, b| max(a, b) });
				// 	inputArray/maxVal;
				// }],
				["Normalise - bipolar (default)", {arg inputArray; inputArray.normalize2;}],
				["Normalise", {arg inputArray; inputArray.normalize(-1.0, 1.0);}],
				["Fold", {arg inputArray; inputArray.fold(-1.0, 1.0);}],
				["Wrap", {arg inputArray; inputArray.wrap(-1.0, 1.0);}],
				["Clip", {arg inputArray; inputArray.clip(-1.0, 1.0);}],
			],
			[ // terrain mode
				["Terrain Code", 0],
				["Terrain Builder", 1],
				["Terrain From Image", 2],
			],
			[ // filter functions
				["Off (default)", {arg sig; sig;}],
				["Median filter - 3 samples", {arg sig; Median.ar(3, sig);}],
				["Median filter - 5 samples", {arg sig; Median.ar(5, sig);}],
				["Median filter - 7 samples", {arg sig; Median.ar(7, sig);}],
				["Median filter - 9 samples", {arg sig; Median.ar(9, sig);}],
				["Median filter - 11 samples", {arg sig; Median.ar(11, sig);}],
				["Median filter - 13 samples", {arg sig; Median.ar(13, sig);}],
				["Median filter - 15 samples", {arg sig; Median.ar(15, sig);}],
				["Median filter - 17 samples", {arg sig; Median.ar(17, sig);}],
				["Median filter - 19 samples", {arg sig; Median.ar(19, sig);}],
				["Median filter - 21 samples", {arg sig; Median.ar(21, sig);}],
				["Median filter - 23 samples", {arg sig; Median.ar(23, sig);}],
				["Median filter - 25 samples", {arg sig; Median.ar(25, sig);}],
				["Median filter - 27 samples", {arg sig; Median.ar(27, sig);}],
				["Median filter - 29 samples", {arg sig; Median.ar(29, sig);}],
				["Median filter - 31 samples", {arg sig; Median.ar(31, sig);}],
				["Smoothing filter - 3 samples", {arg sig;
					var slope = SampleRate.ir * 3.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 5 samples", {arg sig;
					var slope = SampleRate.ir * 5.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 7 samples", {arg sig;
					var slope = SampleRate.ir * 7.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 9 samples", {arg sig;
					var slope = SampleRate.ir * 9.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 11 samples", {arg sig;
					var slope = SampleRate.ir * 11.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 13 samples", {arg sig;
					var slope = SampleRate.ir * 13.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 15 samples", {arg sig;
					var slope = SampleRate.ir * 15.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 17 samples", {arg sig;
					var slope = SampleRate.ir * 17.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 19 samples", {arg sig;
					var slope = SampleRate.ir * 19.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 21 samples", {arg sig;
					var slope = SampleRate.ir * 21.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 23 samples", {arg sig;
					var slope = SampleRate.ir * 23.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 25 samples", {arg sig;
					var slope = SampleRate.ir * 25.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 27 samples", {arg sig;
					var slope = SampleRate.ir * 27.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 29 samples", {arg sig;
					var slope = SampleRate.ir * 29.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 31 samples", {arg sig;
					var slope = SampleRate.ir * 31.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
			],
			 // amp compensation
			TXAmpComp.arrOptionData,
		];
		synthDefFunc = {
			arg out, bufnumTerrain, freq, freqMin, freqMax,
			change1, change1Min, change1Max, change2, change2Min, change2Max,
			change3, change3Min, change3Max, change4, change4Min, change4Max,
			outLevel,
			modFreq = 0, modChange1 = 0, modChange2 = 0, modChange3 = 0, modChange4 = 0,
			modOutLevel = 0;

			var outFreq, outChange1, outChange2, outChange3, outChange4;
			var outXY, holdFilterFunc, sumOutLevel, ampCompFunction, outAmpComp, outWT;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			outFreq = ( (freqMax/freqMin) ** ((freq + modFreq).max(0.001).min(1)) ) * freqMin;
			outChange1 = change1Min + ((change1Max - change1Min) * (change1 + modChange1).max(0).min(1));
			outChange2 = change2Min + ((change2Max - change2Min) * (change2 + modChange2).max(0).min(1));
			outChange3 = change3Min + ((change3Max - change3Min) * (change3 + modChange3).max(0).min(1));
			outChange4 = change4Min + ((change4Max - change4Min) * (change4 + modChange4).max(0).min(1));
			outXY = orbitCodeString.compile.value.value(outFreq, outChange1, outChange2, outChange3, outChange4);
			holdFilterFunc = this.getSynthOption(2);
			sumOutLevel = (outLevel + modOutLevel).max(0).min(1);
			ampCompFunction = this.getSynthOption(3);
			outAmpComp = ampCompFunction.value(outFreq);
			// fold X & Y to range 0-1
			outWT = WaveTerrain.ar(bufnumTerrain, outXY.at(0).fold(0, 1), outXY.at(1).fold(0, 1),
				classData.terrainWidth, classData.terrainWidth);
			// use TXClean to stop blowups
			Out.ar(out, TXClean.ar(
				LeakDC.ar(startEnv * sumOutLevel * outAmpComp
					*  holdFilterFunc.value(outWT), 0.995);
			));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXMinMaxSliderSplit", "Modify 1", \unipolar, "change1", "change1Min", "change1Max"],
			["TXMinMaxSliderSplit", "Modify 2", \unipolar, "change2", "change2Min", "change2Max"],
			["TXMinMaxSliderSplit", "Modify 3", \unipolar, "change3", "change3Min", "change3Max"],
			["TXMinMaxSliderSplit", "Modify 4", \unipolar, "change4", "change4Min", "change4Max"],
			["SynthOptionPopupPlusMinus", "Filter", arrOptionData, 2, 360, {this.rebuildSynth;}],
			["EZSlider", "Out Level", \unipolar, "outLevel", nil, 500],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 3],
			["TXMinMaxFreqNoteSldr", "Freq", classData.freqSpec,
				"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges, 500],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.rebuildTerrain;
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Orbit Controls", {displayOption = "showOrbitControls";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showOrbitControls")],
			["Spacer", 3],
			["ActionButton", "Orbit Type", {displayOption = "showOrbit";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showOrbit")],
			["Spacer", 3],
			["ActionButton", "Terrain", {displayOption = "showTerrain";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showTerrain")],
			["Spacer", 3],
			["ActionButton", "Terrain Image", {displayOption = "showImage";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showImage")],
			["Spacer", 3],
			["SpacerLine", 4],
		];
		if (displayOption == "showOrbitControls", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxFreqNoteSldr", "Freq", classData.freqSpec,
					"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges, 500],
				["SpacerLine", 16],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "change1", "change1Min", "change1Max",
					nil, nil, nil, nil, 500],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "change2", "change2Min", "change2Max",
					nil, nil, nil, nil, 500],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 3", \unipolar, "change3", "change3Min", "change3Max",
					nil, nil, nil, nil, 500],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 4", \unipolar, "change4", "change4Min", "change4Max",
					nil, nil, nil, nil, 500],
				["SpacerLine", 16],
				["SynthOptionPopupPlusMinus", "Filter", arrOptionData, 2, 360, {this.rebuildSynth;}],
				["SpacerLine", 16],
				["EZSlider", "Out Level", \unipolar, "outLevel", nil, 500],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 3],
			];
		});
		if (displayOption == "showOrbit", {
			guiSpecArray = guiSpecArray ++[
				["TXPresetPopup", "Presets",
					arrOrbitPresets.collect({arg item, i; item.at(0)}),
					arrOrbitPresets.collect({arg item, i; item.at(1)}),
					360,
				],
				["SpacerLine", 1],
				["TextViewDisplay", "Orbit Coding Notes: The Supercollider 3 code below should be a function which is passed Frequency & Modify 1-4, and returns an array of audio rate UGens as X & Y values between 0 and 1. Values outside that range will be folded (i.e. reflected) back in range.",
					665, 50, "Notes"],
				["TextViewCompile", orbitCodeString, {arg argText; this.evaluateOrbitCode(argText);}, 300, 300, nil, 100],
			];
		});
		if (displayOption == "showTerrain", {
			// terrain code
			if (this.getSynthOption(1) == 0, {
				guiSpecArray = guiSpecArray ++[
					["SynthOptionPopupPlusMinus", "Terrain mode", arrOptionData, 1, 360,
						{this.rebuildTerrain; this.buildGuiSpecArray; system.showView;}],
					["SpacerLine", 10],
					["TXPresetPopup", "Presets",
						arrTerrainPresets.collect({arg item, i; item.at(0)}),
						arrTerrainPresets.collect({arg item, i; item.at(1)}),
						360,
					],
					["SpacerLine", 1],
					["TextViewDisplay", "Terrain Coding Notes: The Supercollider 3 code below should be a function which is passed arguments of X value and Y value, and returns a single sample value between -1 and 1. Values outside that range will be changed using the function chosen in Range adjust.",
						665, 50, "Notes"],
					["TextViewCompile", terrainCodeString,
						{arg argText; this.evaluateTerrainCode(argText); this.buildGuiSpecArray; system.showView;},
						300, 300, nil, 100],
					["ImageBox", terrainImage, 256, 256],
					["SpacerLine", 10],
					["SynthOptionPopupPlusMinus", "Range adjust", arrOptionData, 0, 360,
						{this.rebuildTerrain; this.buildGuiSpecArray; system.showView;}],
				];
			}, {
				// terrain builder
				if (this.getSynthOption(1) == 1, {
					guiSpecArray = guiSpecArray ++[
						["SynthOptionPopupPlusMinus", "Terrain mode", arrOptionData, 1, 360,
							{this.rebuildTerrain; this.buildGuiSpecArray; system.showView;}],
						["SpacerLine", 10],
						// DragDrop
						// arguments- index1 is string to display,
						// index2 is canReceiveDragHandler function
						// 	index3 is receiveDragHandler function
						// index 4 is optional showDraggedContent (default false)
						// index 5 is optional width (default 100)
						// index 6 is optional height (default 30)
						// index 7 is optional begin drag action function
						// index 8 is optional drag label
						["DragDrop", "Drag & Drop Wave Terrain", {View.currentDrag.isKindOf(Array);},
							{arg view;
								var dropData = View.currentDrag.value;
								builderWTData = dropData[1];
								builderTerrain = dropData[2];
								this.rebuildTerrain;
								system.showView;
							},
							false,
							256, 30,
							{arg view;
								var dragData;
								if (builderWTData.notNil, {
									dragData = [\TXWaveTerrainPresetData, builderWTData, builderTerrain];
									view.dragLabel  = "Wave Terrain";

								}, {
									dragData = nil;
									view.dragLabel = "Empty";
								});
								dragData;
							},
							"Wave Terrain",
						],
						["Spacer", 10],
						["ActionButtonBig", "Open Wave Terrain Builder", {TXWaveTerrainBuilder.showWindow;}, 170,
							TXColor.white, TXColor.sysGuiCol2],
						["SpacerLine", 10],
						["ImageBox", terrainImage, 256, 256],
					];
				}, {
				// terrain From Image
					guiSpecArray = guiSpecArray ++[
						["SynthOptionPopupPlusMinus", "Terrain mode", arrOptionData, 1, 360,
							{this.rebuildTerrain; this.buildGuiSpecArray; system.showView;}],
						["SpacerLine", 10],
						["ActionButtonBig", "Open PNG file", {this.openNewFile}],
						["DragSink", "Drag PNG image file here to import", {View.currentDrag.notNil;},
							{var dragData = View.currentDrag; this.importImage(dragData); system.showView;}, false, 220, 30],
						["Spacer", 10],
						["TextViewDisplay",
							"Imported image will be converted to greyscale, representing terrain values from -1 to +1.",
							500, 30, "Notes"],
						["SpacerLine", 10],
						["ImageBox", terrainImage, 256, 256],
					];
				});
			});
		});
		if (displayOption == "showImage", {
			guiSpecArray = guiSpecArray ++[
				["TextViewDisplay", "Image created by plotting the terrain from black through grey to white, showing terrain values from -1 to +1.", 600, 30, "Notes"],
				["SpacerLine", 4],
				["ImageBox", terrainImage, 384, 384],
			];
		});
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	updateRangeFunctionOption{ arg argOption;
		arrOptions.put(0, argOption);
	}

	evaluateTerrainCode {arg argText;
		var compileResult, holdText;
		holdText = argText.deepCopy;
		compileResult = holdText.compile;
		if (compileResult.isNil, {
			TXInfoScreen.new("ERROR: code will not compile - see post window ");
		},{
			terrainCodeString = holdText;
			this.rebuildTerrain;
		});
	}

	evaluateOrbitCode {arg argText;
		var compileResult, holdText;
		holdText = argText.copy;
		compileResult = holdText.compile;
		if (compileResult.isNil, {
			TXInfoScreen.new("ERROR: code will not compile - see post window ");
		},{
			orbitCodeString = holdText;
			this.rebuildSynth;
		});
	}

	rebuildTerrain {
		var width = classData.terrainWidth; //= num cols
		var height = classData.terrainWidth; // = num rows, though indexing bottom to top. i.e., standard Cartesian co-ords
		var holdCodeFunction, holdRangeFunction;
		// terrain code
		if (this.getSynthOption(1) == 0, {
			holdCodeFunction = terrainCodeString.compile;
			terrainArray = Array.fill(width * height,{
				arg i;
				var xnow, ynow, x, y, holdVal;
				xnow = i%width;
				ynow = (i-xnow).div(width);
				x = xnow/width;
				y = ynow/height;
				holdVal = holdCodeFunction.value.value(x,y);
				if (holdVal.isNaN or: {holdVal.isNumber.not} or: {holdVal == inf}
					or: {holdVal == -inf}, {
						holdVal = 0;
				});
				holdVal;
			});
			holdRangeFunction = this.getSynthOption(0);
			terrainArray = holdRangeFunction.value(terrainArray);
		}, {
			// terrain builder
			if (this.getSynthOption(1) == 1, {
				terrainArray = builderTerrain ? Array.fill(width * height, 0);
			}, {
				// terrain from image
				if (this.getSynthOption(1) == 2, {
					terrainArray = importedImageTerrain ? Array.fill(width * height, 0);
				});
			});
		});
		this.rebuildImageFromTerrain;
		//buffers.at(0).loadCollection(terrainArray);   // load sometimes gives errors - try send
		buffers.at(0).sendCollection(terrainArray);
		this.rebuildSynth;
	}

	initialiseTerrainImage {
		if (terrainImage.isNil or: {terrainImage.width != classData.terrainWidth}, {
			if (terrainImage.notNil, {
				terrainImage.free;
			});
			terrainImage = Image.new(classData.terrainWidth @ classData.terrainWidth);
		});
	}

	rebuildImageFromTerrain {
		var holdVal, holdColor, colA, colB, colC, holdArray;
		//defer
		{
			this.initialiseTerrainImage;
			// add data to image with color
			colA = Color.black;
			colB = Color.grey;
			colC = Color.white;
			holdArray = Int32Array.fill(classData.terrainWidth * classData.terrainWidth, {arg i;
				holdVal = terrainArray[i];
				holdColor = this.getColorFromTerrainVal(holdVal, colA, colB, colC);
				this.getRGBAToInt(
					(holdColor.red * 255).round(1).asInteger,
					(holdColor.green * 255).round(1).asInteger,
					(holdColor.blue * 255).round(1).asInteger,
					255
				);
			});
			terrainImage.pixels_(holdArray);
		}.defer;
	}

	getColorFromTerrainVal {arg val, colA, colB, colC;
		var outColour;
		// value range -1 to 1
		if (val < 0, {
			outColour = colA.blend(colB, val + 1);
		}, {
			outColour = colB.blend(colC, val);
		});
		^outColour;
	}

	getRGBAToInt {arg r, g, b, a;
		^((a << 24) | (r << 16) | (g << 8) | b);
	}

	get32BitIntToRGB {arg int32;
		var hold_red, hold_green, hold_blue;
		hold_red = (int32 >> 16) & 0x0ff;
		hold_green =  (int32 >> 8) & 0x0ff;
		hold_blue =  int32 & 0x0ff;
		^[hold_red, hold_green, hold_blue];
	}

	openNewFile {
		// get path/filename
		Dialog.openPanel({ arg path;
			this.importImage(path);
			system.showView;
		});
	}

	isValidImageFile {arg argPath;  // check argument is a valid path
		var holdPathName, errorMessage;
		holdPathName = PathName.new(argPath.asString).absolutePath;
		if (holdPathName.extension.icontainsStringAt(0, "png"), {
			if (File.exists(holdPathName).not, {
				errorMessage = "Error: File not found: " ++ argPath;
			});
		}, {
			errorMessage = "Error: Not a PNG image filename:  " ++ argPath;
		});
		if (errorMessage.notNil, {
			TXInfoScreen(errorMessage);
			^false;
		});
		^true;
	}

	importImage {arg imageName;
		var tempImage, holdColor, holdPixels;
		{ // defer
			//("importImage: " ++ imageName).postln;
			if (this.isValidImageFile(imageName), {
				tempImage = Image.open(imageName);
				this.initialiseTerrainImage;
				terrainImage.draw({arg image;
					tempImage.drawInRect(Rect(0, 0, image.width, image.height));
				});
				tempImage.free;
				importedImageFilename = imageName;
				importedImageTerrain = terrainImage.pixels.collect({arg item;
					(this.get32BitIntToRGB(item).sum * 2 * (3 * 255).reciprocal) - 1;
				});
				this.rebuildTerrain;
			});
		}.defer;
	}

	///////////

	extraSaveData { // override default method
		^[terrainCodeString, orbitCodeString, builderTerrain, builderWTData, importedImageTerrain, importedImageFilename];
	}

	loadExtraData {arg argData, isPresetData;  // override default method
		terrainCodeString = argData.at(0);
		orbitCodeString = argData.at(1);
		builderTerrain = argData.at(2);
		builderWTData = argData.at(3);
		importedImageTerrain = argData.at(4);
		importedImageFilename = argData.at(5);
		if (isPresetData == true, {
			this.rebuildTerrain;
			this.buildGuiSpecArray;
			system.showView;
		});
	}

}

