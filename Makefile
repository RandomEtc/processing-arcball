
PROCESSING_CORE = /Applications/Processing.app/Contents/Resources/Java/core.jar

SRC_FILES = ArcBall.java

library/arcball.jar: $(SRC_FILES) Makefile
	javac -cp $(PROCESSING_CORE) -sourcepath src -d . -target 1.1 -source 1.3 $(SRC_FILES) 
	jar -Mcvf library/arcball.jar com/processinghacks/arcball/*.class

clean:
	rm -f library/arcball.jar
	rm -rf com

