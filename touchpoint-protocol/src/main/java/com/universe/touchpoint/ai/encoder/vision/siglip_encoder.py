import torch
import numpy as np
from PIL import Image
from transformers import AutoImageProcessor, AutoModel

# 提取 SigLIP 特征
def extract_siglip_features(image_data, model):
    # 加载 SigLIP 模型和处理器
    processor = AutoImageProcessor.from_pretrained("")
    model = AutoModel.from_pretrained("google/siglip-base-patch16-224")
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model.to(device)

    # 将 NumPy 数组转换为 PIL 图像
    image = Image.fromarray(image_data.astype(np.uint8))

    # 将图像数据转换为模型需要的格式
    inputs = processor(images=image, return_tensors="pt").to(device)
    with torch.no_grad():
        outputs = model(**inputs)
        features = outputs.last_hidden_state.mean(dim=1).cpu().numpy()

    return features