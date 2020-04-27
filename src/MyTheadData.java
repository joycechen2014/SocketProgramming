class MyThreadData{
    private int random;
    private static ThreadLocal<MyThreadData> localmap = new ThreadLocal<MyThreadData>();
    @SuppressWarnings("unused")
    public static MyThreadData getThreadInstance(){
        MyThreadData mythread = localmap.get();
        if(mythread == null){
            mythread = new MyThreadData();
            localmap.set(mythread);
        }
        return mythread;
    }


    public int getData() {
        return random;
    }


    public void setData(int data) {
        this.random = data;
    }

}