def call(Map config = [:]) {
    def pipeline = new Javacodecompile(this)
    pipeline.run(config)
}
