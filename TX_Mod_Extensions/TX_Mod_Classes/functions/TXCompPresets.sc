// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXCompPresets {

*initClass{
	//
	// set class specific variables
	//
}

*arrCompPresets { arg argModule;
	^[
		[	"Full reset - no compression or expansion",
			{	argModule.setSynthValue("threshold", 0.5);
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"1.5-1 compression",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(1.5));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"2-1 compression",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(2));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"3-1 compression",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(3));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"5-1 compression",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(5));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"10-1 compression",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(10));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"1.5-1 expansion",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(1.5));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"2-1 expansion",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(2));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"3-1 expansion",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(3));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"5-1 expansion",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(5));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
		[	"gate",
			{
				argModule.setSynthValue("outGain", 1);
				argModule.setSynthValue("expanderRatio",ControlSpec(0.1, 10).unmap(10));
				argModule.setSynthValue("expanderRatioMin", 0.1);
				argModule.setSynthValue("expanderRatioMax", 10);
				argModule.setSynthValue("compressorRatio",ControlSpec(0.1, 10).unmap(1));
				argModule.setSynthValue("compressorRatioMin", 0.1);
				argModule.setSynthValue("compressorRatioMax", 10);
				argModule.setSynthValue("attack", 0.1);
				argModule.setSynthValue("attackMin", 0.001);
				argModule.setSynthValue("attackMax", 0.1);
				argModule.setSynthValue("release", 0.1);
				argModule.setSynthValue("releaseMin", 0.01);
				argModule.setSynthValue("releaseMax", 0.3);
			},
		],
	];
}

}

