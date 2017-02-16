package de.spricom.dessert.classfile;

class ExceptionTableEntry {
    private int startPc;
    private int endPc;
    private int handlerPc;
    private String catchType;

    public int getStartPc() {
        return startPc;
    }

    public void setStartPc(int startPc) {
        this.startPc = startPc;
    }

    public int getEndPc() {
        return endPc;
    }

    public void setEndPc(int endPc) {
        this.endPc = endPc;
    }

    public int getHandlerPc() {
        return handlerPc;
    }

    public void setHandlerPc(int handlerPc) {
        this.handlerPc = handlerPc;
    }

    public String getCatchType() {
        return catchType;
    }

    public void setCatchType(String catchType) {
        this.catchType = catchType;
    }
}
