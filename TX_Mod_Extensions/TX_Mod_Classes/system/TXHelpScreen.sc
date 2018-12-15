// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXHelpScreen {
	classvar <>classData;

	// for testing
	// TXHelpScreen.open;

	*initClass {
		classData = ();
		classData.alwaysOnTop = 1;
		classData.wideView = 0;
		classData.defaultWidth = 1028; // window w
		classData.defaultHeight = 624; // window h
		classData.webViewWidth = 480;
		classData.imageViewWidth = 508;
		classData.imageIndex = 0;
	}

	*open{ arg winColour, inLeft=50, inTop=500;
		var b,c,d,e,f,g,h,i, j, k, t, u;
		var arrInfoLinesHeight, imagePath;

		// init
		imagePath = PathName.new(this.class.filenameSymbol.asString.dirname ++ "/TX_Mod_Help/Help_Images/");
		classData.imageFiles = imagePath.files.collect({arg item; item.fullPath});
		// defer
		{
			if (classData.window.isNil, {
				classData.defaultWidth = 1028;
				classData.webViewWidth = 480;
				classData.imageViewWidth = 508;
				winColour = winColour ? TXColor.sysHelpCol.blend(TXColor.grey(0.92), 0.83);
				classData.window = Window("TX Modular System - Help",
					Rect(inLeft, inTop, classData.defaultWidth, classData.defaultHeight));
				classData.window.view.decorator = FlowLayout(classData.window.view.bounds);
				classData.window.view.decorator.margin = 10 @ 10;
				classData.window.view.background = winColour;
				classData.window.alwaysOnTop = classData.alwaysOnTop.asBoolean;
				classData.window.front;
				classData.window.onClose = {classData.window = nil};

				classData.window.view.decorator.nextLine;
				e = Button(classData.window, Rect(0,0, 25, 24));
				e.states = [["< ", Color.white, TXColor.sysGuiCol1]];
				e.action = {classData.webView.back };

				f = Button(classData.window, Rect(0,0, 25, 24));
				f.states = [["> ", Color.white, TXColor.sysGuiCol1]];
				f.action = {classData.webView.forward};

				classData.window.view.decorator.shift(20,0);

				b = Button(classData.window, Rect(0,0, 78, 24));
				b.states = [["Help Index ", Color.white, TXColor.sysHelpCol]];
				b.action = {this.goToIndex;};

				classData.window.view.decorator.shift(20,0);

				u = StaticText(classData.window, Rect(0,0, 50, 24));
				u.align = 'right';
				u.string = "Find: ";
				u.stringColor_(Color.green(0.5));
				u.background_(Color.white);

				t = TextField(classData.window, Rect(0,0, 125, 24));
				t.action = {classData.webView.findText(t.string)};

				b = Button(classData.window, Rect(0,0, 20, 24));
				b.states = [["x ", Color.green(0.5), Color.white]];
				b.action = {t.string = "";};

				b = Button(classData.window, Rect(0,0, 36, 24));
				b.states = [["Next ", Color.green(0.5), Color.white]];
				b.action = {classData.webView.findText(t.string)};

				b = Button(classData.window, Rect(0,0, 36, 24));
				b.states = [["Prev ", Color.green(0.5), Color.white]];
				b.action = {classData.webView.findText(t.string, true)};

				classData.window.view.decorator.shift(26,0);

				h = TXCheckBox(classData.window, Rect(0, 0, 126, 24), "window on top", TXColor.sysGuiCol1,
					TXColour.white, TXColor.white, TXColor.sysGuiCol1);
				h.value =  classData.alwaysOnTop;
				h.action = {|view|
					classData.alwaysOnTop = view.value;
					classData.window.alwaysOnTop = classData.alwaysOnTop.asBoolean;
				};

				classData.window.view.decorator.shift(26,0);

				j = TXCheckBox(classData.window, Rect(0, 0, 116, 24), "wide window", TXColor.sysGuiCol1,
					TXColour.white, TXColor.white, TXColor.sysGuiCol1);
				j.value =  classData.wideView;
				j.action = {|view|
					classData.wideView = view.value;
					this.changeTextWidth;
				};

				classData.window.view.decorator.nextLine.shift(0, 5);

				classData.helpNameView = StaticText(classData.window, Rect(0,0, classData.webViewWidth, 30));
				classData.helpNameView.stringColor_(Color.grey(0.2));
				classData.helpNameView.background_(Color.white);
				classData.helpNameView.font_( Font("Helvetica", 14));
				classData.helpNameView.align_(\center);

				classData.window.view.decorator.shift(10,0);

				// make group for moving
				classData.group1 = View.new(classData.window, Rect(0,0, 514, 30));
				classData.imagesText = StaticText(classData.group1, Rect(0,0, 60, 30))
				.stringColor_(Color.grey(0.2))
				.background_(Color.white)
				.string_("Images: ")
				.align_(\center);

				classData.imageNamePopUp = PopUpMenu(classData.group1, Rect(64,0, 300, 30));
				classData.imageNamePopUp.items = this.getImageFileNames;
				classData.imageNamePopUp.action = {arg view; classData.imageIndex = view.value; this.updateImageView};

				Button(classData.group1, Rect(368,0, 20, 30))
				.states_([
					["+", Color.white, TXColor.sysGuiCol1],
				])
				.action_({ arg butt;
					classData.imageNamePopUp.valueAction =
					(classData.imageNamePopUp.value + 1).min(classData.imageNamePopUp.items.size - 1);
				});
				Button(classData.group1, Rect(392,0, 20, 30))
				.states_([
					["-", Color.white, TXColor.sysGuiCol1],
				])
				.action_({ arg butt;
					classData.imageNamePopUp.valueAction = (classData.imageNamePopUp.value - 1).max(0);
				});

				classData.window.view.decorator.nextLine.shift(0, 5);

				classData.webView = WebView(classData.window, Rect(0,0, classData.webViewWidth, 530));
				// classData.webView.onLoadFailed = {arg view;("Failed to load URL: " ++ view.url.asString).postln;};
				classData.webView.onLoadFinished = {arg view;
					var holdString = view.url.asString;
					holdString = holdString.asPathName.fileNameWithoutExtension;
					holdString = holdString.replace("TX_0", "").replace("TX_", "");
					holdString = holdString.replace("_", " ");
					classData.helpNameView.string = holdString;
					this.openImageForName(holdString);
				};
				//classData.webView.onLinkActivated = {arg view, url; classData.helpNameView.string = url.asString; classData.webView.url = url};
				classData.webView.resize_(4);

				classData.window.view.decorator.shift(10,0);
				classData.imageScrollView = ScrollView(classData.window, Rect(0,0, classData.imageViewWidth, 530));
				classData.imageScrollView.resize_(5);
				classData.compositeView = CompositeView(classData.imageScrollView, Rect(0,0, 1860, 1440));
				classData.imageView = UserView(classData.compositeView, Rect(0,0, 1860, 1440));
				//classData.imageView.background_(Color.white);

				this.goToIndex;
				this.updateImageView;
				// adjust for width
				if (classData.wideView == 1, {
					{this.changeTextWidth}.defer(0.03);
				});
			}, {
				classData.window.front;
			});
		}.defer;
	}

	*goToIndex {arg indexString = "/TX_Mod_Help/TX_0 TX Modular Help.html";
		{
			classData.webView.url = this.class.filenameSymbol.asString.dirname ++ indexString;
			classData.imageIndex = 0;
			classData.imageNamePopUp.value = 0;
			this.updateImageView;
		}.defer;
	}

	*openFile {arg helpFileName, inLeft=50, inTop=500;
		var holdString;
		{
			this.open(inLeft: inLeft, inTop: inTop);
			holdString = this.class.filenameSymbol.asString.dirname ++ "/TX_Mod_Help/" ++ helpFileName ++ ".html";
			//("Opening Help File: " ++ holdString).postln;
			classData.webView.url = holdString;
			this.openImageForName(helpFileName);
		}.defer;
	}

	*openFilePath {arg filePath, inLeft=50, inTop=500;
		var holdString;
		{
			this.open(inLeft: inLeft, inTop: inTop);
			holdString = filePath;
			//("Opening Help File: " ++ holdString).postln;
			classData.webView.url = holdString;
			//this.openImageForName(xxx);
		}.defer;
	}

	*openModule {arg moduleName, inLeft=50, inTop=500;
		var holdString;
		{
			this.open(inLeft: inLeft, inTop: inTop);
			holdString = this.class.filenameSymbol.asString.dirname ++ "/TX_Mod_Help/Modules/" ++ moduleName ++ ".html";
			//("Opening Help File: " ++ holdString).postln;
			classData.webView.url = holdString;
			this.openImageForName(moduleName);
		}.defer;
	}

	*updateImageView {
		classData.image = Image.new(classData.imageFiles[classData.imageIndex]);
		classData.compositeView.bounds.width = classData.image.width;
		classData.compositeView.bounds.height = classData.image.height;
		classData.imageView.bounds.width = classData.image.width;
		classData.imageView.bounds.height = classData.image.height;
		classData.imageView.drawFunc_({classData.image.drawAtPoint(0 @ 0);});
		classData.imageView.refresh;
		classData.imageScrollView.visibleOrigin_(0 @ 0);
	}

	*getImageFileNames {
		var fileNames;
		fileNames = classData.imageFiles.collect({arg item, i; item.basename; });
		^fileNames;
	}

	*openImageForName {arg argName;
		var holdIndex = nil;
		var names = this.getImageFileNames;
		names.do({arg item, i;
			if (item.keep(argName.size) == argName, {
				classData.imageNamePopUp.valueAction = i;
				^0; // return early if image found
			});
		});
	}

	*close {		//	close window
		// OLD if (classData.window.isClosed.not, {classData.window.close;});
		// NEW:
		if (classData.window.notNil,{
			if (classData.window.isClosed.not, {{classData.window.close; classData.window = nil;}.defer;});
		});
	}

	*changeTextWidth {
		var holdBounds;
		if (classData.wideView == 1, {
			// adjust text
			holdBounds = classData.helpNameView.bounds;
			holdBounds.width = holdBounds.width + 200;
			classData.helpNameView.bounds = holdBounds;
			// adjust group
			classData.group1.allChildren.do({arg view;
				holdBounds = view.bounds;
				holdBounds.left = holdBounds.left + 100;
				view.bounds = holdBounds;
			});
			// adjust webView
			holdBounds = classData.webView.bounds;
			holdBounds.width = holdBounds.width + 200;
			classData.webView.bounds = holdBounds;
			// adjust imageScrollView
			holdBounds = classData.imageScrollView.bounds;
			holdBounds.width = (holdBounds.width - 200).max(1);
			holdBounds.left = holdBounds.left + 200;
			classData.imageScrollView.bounds = holdBounds;
			// adjust window
			classData.window.setInnerExtent(classData.window.bounds.width + 200,
				classData.window.bounds.height);
		}, {
			// adjust text
			holdBounds = classData.helpNameView.bounds;
			holdBounds.width = holdBounds.width - 200;
			classData.helpNameView.bounds = holdBounds;
			// adjust group
			classData.group1.allChildren.do({arg view;
				holdBounds = view.bounds;
				holdBounds.left = holdBounds.left - 100;
				view.bounds = holdBounds;
			});
			// adjust webView
			holdBounds = classData.webView.bounds;
			holdBounds.width = (holdBounds.width - 200).max(1);
			classData.webView.bounds = holdBounds;
			// adjust imageScrollView
			holdBounds = classData.imageScrollView.bounds;
			holdBounds.width = holdBounds.width + 200;
			holdBounds.left = (holdBounds.left - 200).max(1);
			classData.imageScrollView.bounds = holdBounds;
			// adjust window
			classData.window.setInnerExtent(classData.window.bounds.width - 200,
				classData.window.bounds.height);
		});
	}

}
	